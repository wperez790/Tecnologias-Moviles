package org.moviles.activity.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.moviles.Constants;
import org.moviles.Contexto;
import org.moviles.Util;
import org.moviles.activity.Interfaces.IFragmentEditarUsuarioListener;
import org.moviles.activity.R;
import org.moviles.business.UsuarioBusiness;
import org.moviles.model.Usuario;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentEditarUsuario extends Fragment {


    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2 ;
    private boolean permisos = false;
    private CircleImageView imgPerfil;
    private EditText nombre;
    private EditText correo;
    private EditText password;
    private Bitmap bmp = null;
    private IFragmentEditarUsuarioListener onclick;

    public FragmentEditarUsuario(IFragmentEditarUsuarioListener onclick){
        this.onclick = onclick;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contenedor = inflater.inflate(R.layout.fragment_editar_usuario, container, false);
        imgPerfil = contenedor.findViewById(R.id.profile_image);
        FloatingActionButton fabCamera = contenedor.findViewById(R.id.floatingButtonCamera);

        fabCamera.setOnClickListener(
                new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         selectImage();
                     }
                 }
        );

        Button btnGuardar = contenedor.findViewById(R.id.btnModificarUsuario);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });
        nombre = contenedor.findViewById(R.id.nombrePerfil);
        correo = contenedor.findViewById(R.id.correoPerfil);
        password = contenedor.findViewById(R.id.contrasenaPerfil);

        Usuario u = Contexto.getUsuarioBusiness().getCurrentUser();
        nombre.setText(u.getUsuario());
        correo.setText(u.getEmail());
        password.setText(u.getPassword());

        File fl = new File(Contexto.getDataDir(),
                u.getUsuario()+"/"+ Constants.USER_AVATAR);
        if(fl.exists()){
            Bitmap bmp = Util.getImage(fl);
            imgPerfil.setImageBitmap(bmp);
        }

        return contenedor;
    }

    private void guardar(){
        UsuarioBusiness userBO = Contexto.getUsuarioBusiness();
        Usuario currentUser = userBO.getCurrentUser();

        if(bmp != null) {
            File fl = new File(Contexto.getDataDir(),
                    currentUser.getUsuario() + "/" + Constants.USER_AVATAR);

            Util.saveImage(fl, bmp);
        }

        Usuario user = new Usuario();
        user.setUsuario(nombre.getText().toString());
        user.setPassword(password.getText().toString());
        user.setEmail(correo.getText().toString());
        user.setFotoPerfil(bmp);
        //user.setFechaNacimiento("A COMPLETAR");
        boolean valid = Util.checkUser(getActivity().getApplicationContext(),currentUser);

        if(!valid)
            return;

        if(!user.getUsuario().equals(currentUser.getUsuario())) {

            if(userBO.getUsuario(user.getUsuario()) != null){
                Toast.makeText(getActivity().getApplicationContext(),R.string.usuarioYaExiste, Toast.LENGTH_SHORT).show();
                return;
            }

            Util.renameFile(new File(Contexto.getDataDir(), currentUser.getUsuario()), user.getUsuario());
            userBO.changeUserNameList(currentUser.getUsuario(),user.getUsuario());

        }

        currentUser = user;
        if(userBO.update(currentUser)){
            Contexto.getUsuarioBusiness().setCurrentUser(currentUser);
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),R.string.guardado, Toast.LENGTH_SHORT);
            toast.show();
        }
        onclick.actualizarUsuario();
        onclick.cerrarFramgmentEditarUsuario();


    }

    private void selectImage(){
        final String camara = getResources().getString(R.string.camara);
        final String galeria =  getResources().getString(R.string.galeria);
        final String cancelar = getResources().getString(R.string.cancelar);

        final CharSequence[] items = { camara, galeria,cancelar};

        AlertDialog.Builder adBuilder = new AlertDialog.Builder(getActivity());
        adBuilder.setTitle(R.string.agregarImagen);
        adBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (items[i].equals(camara)) {
                    requestPermissionsCamera();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals(galeria)) {
                    requestPermissionsGallery();
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);


                } else if (items[i].equals(cancelar)) {
                    dialogInterface.dismiss();
                }
            }
        });
        adBuilder.show();
    }
    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==REQUEST_CAMERA){

                Bundle bundle = data.getExtras();
                bmp = (Bitmap) bundle.get("data");

                imgPerfil.setImageBitmap(bmp);

            }else if(requestCode==SELECT_FILE){

                Uri selectedImageUri = data.getData();

                bmp = Util.getImageFromGallery(
                        getActivity().getContentResolver(),selectedImageUri);


                imgPerfil.setImageBitmap(bmp);
            }

        }
    }

    public void requestPermissionsCamera(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    PackageManager.GET_PERMISSIONS);
        }


    }
    public void requestPermissionsGallery(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PackageManager.GET_PERMISSIONS);
        }

    }


}
