package org.moviles;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import org.moviles.activity.R;
import org.moviles.business.UsuarioBusiness;
import org.moviles.model.Usuario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Pattern;

public class Util {

    private static Pattern p = Pattern.compile("^[a-zA-Z0-9_]*$");

    public static boolean deleteFileOnPath(File root,String file){
        File dir = new File(root,file);
        if (dir.isDirectory()){
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }

        return dir.delete();
    }

    public static boolean renameFile(File old,String newName){
        File newFile = new File(old.getParentFile(),newName);
        return old.renameTo(newFile);
    }

    public static boolean isAlphaNumeric(String s) {
        return p.matcher(s).find();
    }

    public static String readFile(File file){
        String out = "";
        String line;
        try {
            BufferedReader bf = new BufferedReader(new FileReader(file));
            while ((line=bf.readLine())!= null){
                out += line + "\n";
            }
            out = out.substring(0,out.length()-1);
            bf.close();
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }

        return out;
    }

    public static boolean writeFile(File file, String text){
        try {
            if(!file.exists())
                file.getParentFile().mkdirs();

            FileWriter fl = new FileWriter(file);
            fl.append(text);
            fl.close();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean saveImage(File file, Bitmap img){
        try{
            if(!file.exists())
                file.getParentFile().mkdirs();

            FileOutputStream fos = new FileOutputStream(file);
            img.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static Bitmap getImage(File file){
        if(file.exists()){
            Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
            return bmp;
        }else
            return null;
    }

    public static  Bitmap getImageFromGallery(ContentResolver c, Uri uri){
        Bitmap bmp = null;
        try{
           bmp = MediaStore.Images.Media.getBitmap(c,uri);
        }catch (Exception e){
            e.printStackTrace();
        }

        return bmp;
    }

    public static boolean checkUser(Context context, Usuario u){
        if(u.getUsuario().length() < 5){
            Toast.makeText(context, R.string.nombreMenor5Letras,Toast.LENGTH_LONG).show();
            return false;
        }
        if(u.getUsuario().length() > 15){
            Toast.makeText(context,R.string.nombreMayor15Letras,Toast.LENGTH_LONG).show();
            return false;
        }
        if(!Util.isAlphaNumeric(u.getUsuario().toString())){
            Toast.makeText(context,R.string.nombreNoValido,Toast.LENGTH_LONG).show();
            return false;
        }

        if(u.getPassword().length() < 6){
            Toast.makeText(context,R.string.contrasenaMenor6Letras,Toast.LENGTH_LONG).show();
            return false;
        }
        if(u.getPassword().length() > 20){
            Toast.makeText(context,R.string.contrasenaMayor20Letras,Toast.LENGTH_LONG).show();
            return false;
        }
        if(!Util.isAlphaNumeric(u.getPassword().toString())){
            Toast.makeText(context,R.string.contrasenaNoValido,Toast.LENGTH_LONG).show();
            return false;
        }

        String[] partes = u.getEmail().split("@");
        if(partes.length != 2){
            Toast.makeText(context,R.string.emailIncorrecto,Toast.LENGTH_LONG).show();
            return false;
        }
        if(!Util.isAlphaNumeric(partes[0].replaceAll(".",""))
                || !Util.isAlphaNumeric(partes[1].replaceAll(".",""))){
            Toast.makeText(context,R.string.correoNoValido,Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

}
