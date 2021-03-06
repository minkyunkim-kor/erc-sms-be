package com.erclub.sms.common.jwt;

import com.erclub.sms.common.exception.CommonException;
import com.erclub.sms.models.ErcUser;
import com.erclub.sms.repositories.UserRepository;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtUserDetailsService implements UserDetailsService {

  @Value("${token.secret:erclub}")
  private String secret;

  private final PasswordEncoder passwordEncoder;

  private final UserRepository userRepository;

  public JwtUserDetailsService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    ErcUser user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(id));

    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
    return new User(user.getLoginId(), user.getPwd(), grantedAuthorities);
  }

  public ErcUser authenticateByIdAndPassword(String id, String password) {
    ErcUser user = userRepository.findByLoginId(id).orElseThrow(() -> new UsernameNotFoundException(id));

    if (!passwordEncoder.matches(decrypt(password, secret), user.getPwd())) {
      throw new BadCredentialsException("not matched password");
    }

    if (StringUtils.equalsIgnoreCase(user.getSuspendYn(), "y")) {
      throw new CommonException(HttpStatus.BAD_REQUEST.value(), "suspend user");
    }

    return user;
  }
  private String decrypt(String cipherText, String passphrase) {
    try {
      final int keySize = 256;
      final int ivSize = 128;

      // ???????????? BASE64 ???????????? ????????? ??????.
      byte[] ctBytes = Base64.decodeBase64(cipherText.getBytes("UTF-8"));

      // ????????? ?????????. (????????? 8????????? Salted__ ???????????? ???????????????.)
      byte[] saltBytes = Arrays.copyOfRange(ctBytes, 8, 16);

      // ???????????? ???????????? ?????????.( ????????? ????????? ???????????? ????????? ?????????.)
      byte[] ciphertextBytes = Arrays.copyOfRange(ctBytes, 16, ctBytes.length);

      // ??????????????? ???????????? ?????? IV?????? ????????????.
      byte[] key = new byte[keySize / 8];
      byte[] iv = new byte[ivSize / 8];
      EvpKDF(passphrase.getBytes("UTF-8"), keySize, ivSize, saltBytes, key, iv);

      // ?????????
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
      byte[] recoveredPlaintextBytes = cipher.doFinal(ciphertextBytes);

      return new String(recoveredPlaintextBytes);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }


  private byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
    return EvpKDF(password, keySize, ivSize, salt, 1, "MD5", resultKey, resultIv);
  }

  private byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, int iterations, String hashAlgorithm, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
    keySize = keySize / 32;
    ivSize = ivSize / 32;
    int targetKeySize = keySize + ivSize;
    byte[] derivedBytes = new byte[targetKeySize * 4];
    int numberOfDerivedWords = 0;
    byte[] block = null;
    MessageDigest hasher = MessageDigest.getInstance(hashAlgorithm);
    while (numberOfDerivedWords < targetKeySize) {
      if (block != null) {
        hasher.update(block);
      }
      hasher.update(password);
      // Salting
      block = hasher.digest(salt);
      hasher.reset();
      // Iterations : ??? ????????????(key stretching)
      for (int i = 1; i < iterations; i++) {
        block = hasher.digest(block);
        hasher.reset();
      }
      System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4, Math.min(block.length, (targetKeySize - numberOfDerivedWords) * 4));
      numberOfDerivedWords += block.length / 4;
    }
    System.arraycopy(derivedBytes, 0, resultKey, 0, keySize * 4);
    System.arraycopy(derivedBytes, keySize * 4, resultIv, 0, ivSize * 4);
    return derivedBytes; // key + iv
  }
}
