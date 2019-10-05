package org.moviles.activity.Interfaces;

public interface ListaUsuarioRecyclerViewOnItemClickListener {
    public void onClickItem(int position);
    public void onClickDelete(int position);
    public void onClickIngresar(String user,String password, boolean mantenerSesion);
}
