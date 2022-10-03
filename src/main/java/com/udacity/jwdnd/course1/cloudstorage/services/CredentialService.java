package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public int saveCredentials(Credential credential) {
        int rowsAdded = 0;

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);

        if (credential.getCredentialId() == null) {
            rowsAdded = credentialMapper.insert(credential);
        } else {
            credentialMapper.updateCredential(credential);
        }
        return rowsAdded;
    }

    //this method deletes a credential entry
    public void deleteCredential(Integer credentialId) {
        credentialMapper.delete(credentialId);
    }

    //this method returns list of credentials saved by a particular user
    public List<Credential> getAllCredentials(Integer userId) {
        List<Credential> credentialList = credentialMapper.getCredentialsForUser(userId);
        // Need to grab the decrypted passwords because they may need to be displayed.
        for (Credential credential : credentialList) {
            credential.setDecryptedPassword(encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
        }
        return credentialList;
    }
}
