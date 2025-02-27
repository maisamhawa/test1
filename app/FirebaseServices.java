package com.example.login;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseServices {
    private static FirebaseServices instance;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private FirebaseFirestore fire;

    public FirebaseFirestore getFire()
    {
        return fire;
    }

    public FirebaseStorage getStorage()
    {
        return storage;
    }

    public FirebaseAuth getAuth()
    {
        return auth;
    }

    public FirebaseServices()
    {
        auth=FirebaseAuth.getInstance();
        fire=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
    }
    public static FirebaseServices getInstance()
    {
        if(instance==null)
        {
            instance=new FirebaseServices();
        }
        return instance;
    }
}
