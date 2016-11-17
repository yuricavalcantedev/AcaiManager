package com.heavendevelopment.acaimanager.Service;

import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Yuri on 01/11/2016.
 */

public class UsuarioService {

    public Usuario getUsuario (String login ,String senha){

        List<Usuario> usuarioList = new Select().from(Usuario.class).where("login = ? AND senha = ?", login, senha).execute();

        if(usuarioList == null)
            return null;

        return usuarioList.get(0);
    }

    public Usuario getLoginUsuario (String login){

        List<Usuario> usuarioList = new Select().from(Usuario.class).where("login = ?", login).execute();

        if(usuarioList == null)
            return null;

        return usuarioList.get(0);
    }

    public Usuario getSenhaUsuario (String senha){

        List<Usuario> usuarioList = new Select().from(Usuario.class).where("senha = ?", senha).execute();

        if(usuarioList == null)
            return null;

        return usuarioList.get(0);
    }
}
