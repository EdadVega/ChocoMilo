package com.example.appcrud;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText Codigo ,Descripcion, Precio;
    Button Insertar,Buscar,Modificar, Eliminar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Codigo = (EditText)findViewById(R.id.codigo);
        Descripcion = (EditText)findViewById(R.id.descripcion);
        Precio = (EditText)findViewById(R.id.precio);
        Insertar = (Button)findViewById(R.id.insertar);
        Buscar =(Button)findViewById(R.id.leer);
        Modificar =(Button)findViewById(R.id.modificar);
        Eliminar =(Button)findViewById(R.id.eliminar);

        Insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //el objeto admin es el que ayudara a administtrar la base de datos

                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper((View.OnClickListener) getApplicationContext(),"tienda",null,1);
                // ayudara con la lectura y escritura de labase dedatos
                //metodo getwrie pone la base de datos en modo lectura y escritura
                SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

                //capturar loq ue vamos a guardar

                String codigo =  Codigo.getText().toString();
                String descripcion =  Descripcion.getText().toString();
                String precio  =  Precio.getText().toString();

                //verificar si los campos tienen algun dato
                if(!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){
                    ContentValues registro = new ContentValues();
                    //Ponemos los datos que queremos ingresar en nuestra base de datos
                    registro.put("codigo",codigo);
                    registro.put("descripcion",descripcion);
                    registro.put("precio",precio);

                    //OPCIONAL , LIMPIAR LOS CAMPOS QUE INGRESO EL USUARIO
                    Codigo.setText("");
                    Descripcion.setText("");
                    Precio.setText("");
                    Toast.makeText(getApplicationContext(),"INFORMACION GUARDADA",Toast.LENGTH_SHORT).show();



                    //ingresar los datos
                    //registro tiene los datos que queremos ingresar a la base de datos
                    //lo tiene content values que es registro ↑
                    // recibe  tres parametros , el nombre de la tabla , segundo null , el tercero son los datos qu equeremos ingresar en la tabla
                    BaseDeDatos.insert("articulos",null, registro);

                    //cerrar la base de datos
                    BaseDeDatos.close();


                }else{
                    //Mostramos el error el lenght_SHORT es cuanto tiempo va a durar el mensaje
                    Toast.makeText(getApplicationContext(),"LLENA LOS CAMPOS",Toast.LENGTH_SHORT).show();
                }



            }
        });

        Buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recuperar la informacion del producto al darle al boton buscar
                //establecer la comunicacion con la base de datos
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper((View.OnClickListener) getApplicationContext(),"tienda",null,1);

                //habilitar modo lectura escritura en la base de datos
                SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

                //Establecer que informacion o dato podemos consultar la informacion acerca del producto


                //aca almacenamos lo que tenga el editext del codigo
                String codigo = Codigo.getText().toString();
                if(!codigo.isEmpty()){
                    //crear objeto de la clase cursor
                    // nos ayuda a seleccionar producto atravezs de su codigo
                    //devuelve un arreglo , por eso las posiciones y movetofirst ↓
                    Cursor fila = BaseDeDatos.rawQuery("SELECT descripcion,precio FROM articulos WHERE codigo =" + codigo,null);

                    //si el codigo no existe
                    // Validar que la consulta si devolvio algo : movetofirst
                    if(fila.moveToFirst()){
                        // SI EL ARREGLO CONTIENE VALORES
                        //poner en los editext la informacion sobre descripcion y precioD
                        //la mayoria de las consultas devuleven un arreglo
                        Descripcion.setText(fila.getString(0));
                        Precio.setText(fila.getString(1));
                        //cerrar conexion
                        BaseDeDatos.close();


                    }else{
                        Toast.makeText(getApplicationContext(),"El articulo no existe",Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Debes de introducir el codigo del articulo",Toast.LENGTH_SHORT).show();

                }

            }
        });

        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //establecer conexion base de datos
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper((View.OnClickListener) getApplicationContext(),"tienda",null,1);
                //Habilitar modo lectura escritura
                SQLiteDatabase BaseDeDatos= admin.getWritableDatabase();

                //establecer la informacion que queremos modificar

                //guardar informacion del editext
                String codigo = Codigo.getText().toString();
                String descripcion = Descripcion.getText().toString();
                String precio = Precio.getText().toString();

                //verificar si no vienen vacios
                if (!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){
                    //Establecer la informacion que queremos modificar en la base de datos
                    ContentValues registro = new ContentValues();
                    registro.put("descripcion", descripcion);
                    registro.put("precio", precio);

                    //Modificacion , usando query de sql
                    //ESTO devuelve un valor tipo entero , se guarda en una variable dle mismo tipo para verificar los cambios
                    int validar = BaseDeDatos.update("articulos",registro,"codigo ="+codigo,null);

                    BaseDeDatos.close();

                    if (validar==1){
                        Toast.makeText(getApplicationContext(),"Articulo modificado",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(),"La modificacion no se realizo",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Debes de llenar todos los datos",Toast.LENGTH_SHORT).show();

                }

            }
        });

        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper((View.OnClickListener) getApplicationContext(),"tienda",null,1);
                //Modo lecutra escritura
                SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();
                String codigo = Codigo.getText().toString();

                if (!codigo.isEmpty()){
                    //nombre de la tabla,where
                    int validacion = BaseDeDatos.delete("articulos","codigo="+codigo,null);
                    BaseDeDatos.close();

                    //Poner en blanco los editext
                    Codigo.setText("");
                    Descripcion.setText("");
                    Precio.setText("");

                    //validar si esta eliminando
                    if (validacion==1){
                        Toast.makeText(getApplicationContext(),"Ariculo Eliminado",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(),"No se puedo eliminar",Toast.LENGTH_SHORT).show();
                    }



                }else {
                    Toast.makeText(getApplicationContext(),"Debes de introducr el codigo el articulo",Toast.LENGTH_SHORT).show();

                }

            }
        });



    }
}
//RECORDAR SI DA ERROR , ELIMINAR EL CONTEXTO POR LA PALABRA THIS Y LUEGO EN ADMIN , QUITAN UN CONTEXT CON PARENTESIS