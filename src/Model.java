import java.sql.*;
import java.util.ArrayList;

public class Model implements AutoCloseable  {
        // Objeto Model => base de datos
        private static Model instancia = null;

        // Variables DB MySQL
        private final String URL_JDBC = "jdbc:mysql://localhost:3306/";
        private String USUARIO_DB = "";
        private String CONTRASENA_DB = "";
        private String NOMBRE_DB = "";
        Connection conexion = null;

        private Model(String usuario_bd, String contraseña_bd, String nombre_db) {
            // Cambio credenciales para la bdd en el constructor
            this.USUARIO_DB = usuario_bd;
            this.CONTRASENA_DB = contraseña_bd;
            this.NOMBRE_DB = nombre_db;


            try {
                // Conecto a MySQL
                conexion = DriverManager.getConnection(URL_JDBC, USUARIO_DB, CONTRASENA_DB);
                System.out.println("Conectado a la base de datos");
    
                // Creo una nueva base de datos si no existe
                if (ExisteBaseDeDatos(conexion, NOMBRE_DB) != true) {
                    CrearBaseDeDatos(conexion);
                }
    

            } catch (SQLException e) {
                System.err.println("Hubo un error al conectarse a la base de datos comprueba las credenciales...");
                e.printStackTrace();
            }
        }

        // Destructor custom
        @Override
        public void close() throws Exception {
            // Cierro el curso de la bdd
            conexion.close(); // Cierro cursor
            System.out.println("Cursor BDD Cerrado por destructor...");
            System.out.println("Recursos liberados");
        }

        private void CrearBaseDeDatos(Connection conexion) {
            Statement declaracion = null;
            try {
                declaracion = this.conexion.createStatement();
                declaracion.executeUpdate("CREATE DATABASE " + this.NOMBRE_DB);
                System.out.println("Base de datos creada correctamente");
            } catch (SQLException err) {
                System.err.println("Error al crear la base de datos: " + err.getMessage());
                err.printStackTrace();
            } finally {
                if (declaracion != null) {
                    try {
                        declaracion.close();
                    } catch (SQLException err) {
                        System.err.println("Error al cerrar la declaración: " + err.getMessage());
                    }
                }
            }
        }
    
        private boolean ExisteBaseDeDatos(Connection conexion, String nombre_bd) {
            try {
                DatabaseMetaData meta_datos = conexion.getMetaData();
                ResultSet resultado = meta_datos.getCatalogs();
                while (resultado.next()) {
                    String nombre = resultado.getString(1);
                    if (nombre.equals(nombre_bd)) { // Itero sobre todas las bases de datos
                        resultado.close();
                        return true; // si existe
                    }
                }
                resultado.close();
            } catch (SQLException e) {
                System.err.println("Error al verificar si la base de datos existe: " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }

        public String getURL_JDBC() {
            return URL_JDBC;
        }

        public String getUSUARIO_DB() {
            return USUARIO_DB;
        }

        public String getCONTRASENA_DB() {
            return CONTRASENA_DB;
        }

        public String getNOMBRE_DB() {
            return NOMBRE_DB;
        }

        public void setUSUARIO_DB(String usuario) {
            USUARIO_DB = usuario;
        }

        public void setCONTRASENA_DB(String contraseña) {
            CONTRASENA_DB = contraseña;
        }

        public void setNOMBRE_DB(String nombre_db) {
            NOMBRE_DB = nombre_db;
        }

        public Model getInstancia() {
            return instancia;
        }
        
        public static Model CrearInstancia(String usuario_bd, String contraseña_bd, String nombre_db) {
            // if (instancia == null) { // si instancia no se ha creado lo creamos
                instancia = new Model(usuario_bd, contraseña_bd, nombre_db);
            // }
            return instancia; // sino devuelvo el mismo objeto ya creado
        }

        public boolean ExisteColumnaEnTabla(String tabla, String columna) {
            try {
                DatabaseMetaData meta_datos = conexion.getMetaData();
                ResultSet resultado = meta_datos.getColumns(null, null, tabla, columna);
                boolean existe = resultado.next(); // obtengo el booleano si existe

                resultado.close();
                return existe;
            } catch (SQLException e) {
                System.err.println("Error al verificar si la columna existe en la tabla: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }

        public boolean ExisteTabla(String tabla) {
            try {
                DatabaseMetaData meta_datos = conexion.getMetaData();
                ResultSet set_resultado = meta_datos.getTables(this.NOMBRE_DB, null, tabla.toLowerCase(), null);
                boolean existe = set_resultado.next();
        
                set_resultado.close();
        
                return existe;
            } catch (SQLException err) {
                System.err.println("Error al verificar si la tabla existe en la base de datos: " + err.getMessage());
                err.printStackTrace();
                return false;
            }
        }
        

        public ArrayList<String> GetParametroBDD(String campo, String tabla) {
            ArrayList<String> lst_resultado = new ArrayList<String>();

            try {
                if (!ExisteColumnaEnTabla(tabla, campo)) {
                    System.err.println("La tabla 'usuarios' no existe en la base de datos");
                    return lst_resultado;
                }
    
                // Ejecuto use bdd
                PreparedStatement stat_preparado_use = conexion.prepareStatement("USE "+this.NOMBRE_DB);
                stat_preparado_use.executeUpdate();
                stat_preparado_use.close();

                // Ejecuto consulta SELECT
                PreparedStatement stat_preparado_select = conexion.prepareStatement("SELECT " + campo + " FROM " + tabla);
                ResultSet resultado_set = stat_preparado_select.executeQuery();

                while (resultado_set.next()) { // itero sobre el resultado
                    lst_resultado.add(resultado_set.getString(campo)); // añado a arraylist
                }

                resultado_set.close();
                stat_preparado_select.close();
            } catch (SQLException e) {
                System.err.println("Error al obtener los datos del campo de la tabla: " + e.getMessage());
                e.printStackTrace();
            }

            return lst_resultado;
        }

        public ArrayList<String> GetParametroBDD(String campo, String tabla, String where) {
            /*
             * Sobre carga de función que acepta como argumento una condición where en la sentencia SQL
             */
            ArrayList<String> lst_resultado = new ArrayList<String>();

            try {
                if (!ExisteColumnaEnTabla(tabla, campo)) {
                    System.err.println("La tabla 'usuarios' no existe en la base de datos");
                    return lst_resultado;
                }
    
                // Ejecuto use bdd
                PreparedStatement stat_preparado_use = conexion.prepareStatement("USE "+this.NOMBRE_DB);
                stat_preparado_use.executeUpdate();
                stat_preparado_use.close();

                // Ejecuto consulta SELECT
                PreparedStatement stat_preparado_select = conexion.prepareStatement("SELECT " + campo + " FROM " + tabla + " WHERE " + where);
                ResultSet resultado_set = stat_preparado_select.executeQuery();

                while (resultado_set.next()) { // itero sobre el resultado
                    lst_resultado.add(resultado_set.getString(campo)); // añado a arraylist
                }

                resultado_set.close();
                stat_preparado_select.close();
            } catch (SQLException e) {
                System.err.println("Error al obtener los datos del campo de la tabla: " + e.getMessage());
                e.printStackTrace();
            }

            return lst_resultado;
        }

        
}
