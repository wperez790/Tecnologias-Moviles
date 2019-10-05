package org.moviles.activity.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.moviles.Constants;
import org.moviles.Context;
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

        Usuario u = Context.getUsuarioBusiness().getCurrentUser();
        nombre.setText(u.getUsuario());
        correo.setText(u.getEmail());
        password.setText(u.getPassword());

        File fl = new File(Context.getDataDir(),
                u.getUsuario()+"/"+ Constants.USER_AVATAR);
        if(fl.exists()){
            Bitmap bmp = Util.getImage(fl);
            imgPerfil.setImageBitmap(bmp);
        }

        return contenedor;
    }

    private void guardar(){
        UsuarioBusiness userBO = Context.getUsuarioBusiness();
        Usuario currentUser = userBO.getCurrentUser();

        if(bmp != null) {
            File fl = new File(Context.getDataDir(),
                    currentUser.getUsuario() + "/" + Constants.USER_AVATAR);

            Util.saveImage(fl, bmp);
        }
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Guardado", Toast.LENGTH_SHORT);
        toast.show();

        Usuario user = new Usuario();
        user.setUsuario(nombre.getText().toString());
        user.setPassword(password.getText().toString());
        user.setEmail(correo.getText().toString());
        boolean valid = Util.checkUser(getActivity().getApplicationContext(),currentUser);

        if(!valid)
            return;

        if(!user.getUsuario().equals(currentUser.getUsuario())) {

            if(userBO.getUsuario(user.getUsuario()) != null){
                Toast.makeText(getActivity().getApplicationContext(),"El usuario ya existe", Toast.LENGTH_SHORT).show();
                return;
            }

            Util.renameFile(new File(Context.getDataDir(), currentUser.getUsuario()), user.getUsuario());
            userBO.changeUserNameList(currentUser.getUsuario(),user.getUsuario());

        }

        currentUser = user;
        userBO.update(currentUser);

        onclick.cerrarFramgemntEditarUsuario();

    }

    private void selectImage(){
        final CharSequence[] items = {"Camara", "Galeria", "Cancelar"};

        AlertDialog.Builder adBuilder = new AlertDialog.Builder(getActivity());
        adBuilder.setTitle("Agregar Imagen");
        adBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (items[i].equals("Camara")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Galeria")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);

                } else if (items[i].equals("Cancelar")) {
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
}
