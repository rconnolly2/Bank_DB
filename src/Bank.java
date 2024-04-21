import java.sql.Connection;
import java.util.ArrayList;
import java.sql.*;


public class Bank {
    private Model db_model = null;
    private String nombre_banco = "";
    private Integer cantidad_clientes = 0;
    private Integer cantidad_hipotecas = 0;
    private ArrayList<Clients> clientes_nuevos = new ArrayList<Clients>();
    private ArrayList<String> lst_clientes_db = new ArrayList<String>();

    public Bank(Model modelo, String nom_banco) {
        if (db_model == null) {
            db_model = modelo;
        }
        this.nombre_banco = nom_banco;
        // CREO TABLAS SI NO EXISTEN
        System.out.println(db_model.ExisteTabla("clientes")+"Llega aqui");
        if (!db_model.ExisteTabla("clientes")) {
            CrearTabla("C:\\Users\\Robert\\Desktop\\Java\\Bank_DB\\sql\\clientes.sql");
        } else if (!db_model.ExisteTabla("cuentas")) {
            CrearTabla("C:\\Users\\Robert\\Desktop\\Java\\Bank_DB\\sql\\cuentas.sql");
        }
        ActualizarListaClientes();
    }

    // READ
    private void ActualizarListaClientes() {
        this.db_model.GetParametroBDD("nombre", "usuarios");
        System.out.println("Lista clientes actualizado: READ COMPLETE");
    }

    // CREATE
    private void CrearTabla(String ruta_sql) {
        // Ejecutar script sql clientes
        FileReader fr = new FileReader(db_model, ruta_sql, db_model.getNOMBRE_DB());
    }

    // CREATE
    public void InsertarClientes() {
        try {
            Connection conexion = db_model.conexion;

            // Ejecuto use bdd
            PreparedStatement stat_preparado_use = conexion.prepareStatement("USE "+db_model.getNOMBRE_DB());
            stat_preparado_use.executeUpdate();

            // Preparo la sentencia SQL para verificar si el cliente ya existe QUERY PARAMETIZADO
            String sql_verif = "SELECT COUNT(*) FROM clientes WHERE nombre = ? AND apellido = ? AND correo = ?";
            PreparedStatement statement_verif = conexion.prepareStatement(sql_verif);

            // Preparo la sentencia SQL para insertar un nuevo cliente
            String sql_insertar = "INSERT INTO clientes (nombre, apellido, edad, correo, desempleado, tlf, genero) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement_insertar = conexion.prepareStatement(sql_insertar);

            // Preparo la sentencia SQL para insertar datos en la tabla cuentas
            String sql_insertar_cuenta = "INSERT INTO cuentas (telefono, saldo) VALUES (?, ?)";
            PreparedStatement statement_insertar_cuenta = conexion.prepareStatement(sql_insertar_cuenta);

            for (Clients cli : clientes_nuevos) {
                // verifico si el cliente ya existe
                statement_verif.setString(1, cli.getNombre());
                statement_verif.setString(2, cli.getApellido());
                statement_verif.setString(3, cli.getCorreo());
                ResultSet resultado = statement_verif.executeQuery();
                resultado.next();
                int count = resultado.getInt(1);
                resultado.close();

                if (count == 0) { // Si no hay ninguna columna
                    statement_insertar.setString(1, cli.getNombre());
                    statement_insertar.setString(2, cli.getApellido());
                    statement_insertar.setInt(3, cli.getEdad());
                    statement_insertar.setString(4, cli.getCorreo());
                    statement_insertar.setBoolean(5, cli.isDesempleado());
                    statement_insertar.setInt(6, cli.getTlf());
                    statement_insertar.setString(7, cli.getGenero());

                    // Inserto la cuenta en la tabla cuentas
                    statement_insertar_cuenta.setInt(1, cli.getTlf());
                    statement_insertar_cuenta.setDouble(2, cli.getCant_eur());

                    // ejecuto la query de inserción
                    statement_insertar.executeUpdate();
                    statement_insertar_cuenta.executeUpdate();

                    System.out.println("Cliente insertado correctamente en la tabla clientes y cuentas");
                } else {
                    System.out.println("El cliente " + cli.getNombre() + " " + cli.getApellido() + " ya existe en la base de datos.");
                }
            }

            // Cierro statements
            statement_insertar.close();
            statement_verif.close();    
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente en la tabla clientes o cuentas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void AñadirCliente(Clients cliente_nuevo) {
        String nombre = cliente_nuevo.getNombre();
        String apellido = cliente_nuevo.getApellido();
        boolean existe_cliente = false;

        // Compruebo que este cliente no esta en la lista de clientes que se tiene que subir;
        for (Clients cli : clientes_nuevos) {
            if (cli.getNombre()==nombre && cli.getApellido()==apellido) {
                existe_cliente = true;
                System.err.println("Ya existe este cliente: "+cli.toString());
                break;
            }
        }

        if (!existe_cliente) {
            clientes_nuevos.add(cliente_nuevo);
        }
    }

    public void EnviarBizum(Integer num_emisor, Integer num_receptor, Bank banco_receptor, Double cant_dinero) {
        Connection conexion1 = db_model.conexion;
        Connection conexion2 = banco_receptor.db_model.conexion;
        try {
            // Quito auto_commit
            conexion1.setAutoCommit(false);
            conexion2.setAutoCommit(false);
            
            // Ejecuto use bdd
            PreparedStatement stat_preparado_use_bd1 = conexion1.prepareStatement("USE "+db_model.getNOMBRE_DB());
            stat_preparado_use_bd1.executeUpdate();

            // Realizo operaciones en la primera base de datos
            String sql = "UPDATE cuentas SET saldo = saldo - ? WHERE telefono = ?";
            PreparedStatement statement_bd1 = conexion1.prepareStatement(sql);
    
            // parámetros de la query parametizada
            statement_bd1.setDouble(1, cant_dinero);
            statement_bd1.setInt(2, num_emisor);
    
            // Realizar operaciones en la segunda base de datos

            // Ejecuto use bdd
            PreparedStatement stat_preparado_use_bd2 = conexion2.prepareStatement("USE "+banco_receptor.db_model.getNOMBRE_DB());
            stat_preparado_use_bd2.executeUpdate();

            sql = "UPDATE cuentas SET saldo = saldo + ? WHERE telefono = ?";
            PreparedStatement statement_bd2 = conexion2.prepareStatement(sql);
            statement_bd2.setDouble(1, cant_dinero);
            statement_bd2.setInt(2, num_receptor);

            // Ejecuto queries
            statement_bd1.executeUpdate();
            statement_bd2.executeUpdate();
    
            // hago checkpoint ;)
            conexion1.commit();
            conexion2.commit();
            // Cierro statements
            statement_bd1.close();
            statement_bd2.close();
            stat_preparado_use_bd1.close();    
            stat_preparado_use_bd2.close();    
            System.out.println("Operaciones realizadas con éxito en ambas bases de datos.");
        } catch (SQLException e) {
            // Si se produce un error, realizar rollback
            try {
                if (conexion1 != null) {
                    conexion1.rollback();
                }
                if (conexion2 != null) {
                    conexion2.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Error al realizar operaciones en las bases de datos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // y enciendo autocommit
            try {
                if (conexion1 != null) {
                    conexion1.setAutoCommit(true);
                }
                if (conexion2 != null) {
                    conexion2.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Model getDb_model() {
        return db_model;
    }

    public void setDb_model(Model db_model) {
        this.db_model = db_model;
    }

    public String getNombre_banco() {
        return nombre_banco;
    }

    public void setNombre_banco(String nombre_banco) {
        this.nombre_banco = nombre_banco;
    }

    public Integer getCantidad_clientes() {
        return cantidad_clientes;
    }

    public void setCantidad_clientes(Integer cantidad_clientes) {
        this.cantidad_clientes = cantidad_clientes;
    }

    public Integer getCantidad_hipotecas() {
        return cantidad_hipotecas;
    }

    public void setCantidad_hipotecas(Integer cantidad_hipotecas) {
        this.cantidad_hipotecas = cantidad_hipotecas;
    }

    public ArrayList<Clients> getClientes_nuevos() {
        return clientes_nuevos;
    }

    public void setClientes_nuevos(ArrayList<Clients> clientes_nuevos) {
        this.clientes_nuevos = clientes_nuevos;
    }

    public ArrayList<String> getLst_clientes_db() {
        return lst_clientes_db;
    }

    public void setLst_clientes_db(ArrayList<String> lst_clientes_db) {
        this.lst_clientes_db = lst_clientes_db;
    }


}
