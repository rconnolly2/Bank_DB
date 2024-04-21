import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class FileReader {
    private Model db_model;
    private String ruta_arch = "";
    private String nom_bdd;

    public FileReader(Model obj_modelo, String ruta_archivo, String nom_db) {
        this.db_model = obj_modelo;
        this.ruta_arch = ruta_archivo;
        this.nom_bdd = nom_db;
        EjecutarSQLDesdeArchivo();
    }

    private void EjecutarSQLDesdeArchivo() {
        try {
            File archivoSQL = new File(ruta_arch);
            Scanner sc = new Scanner(archivoSQL);
            SeleccionarBaseDeDatos(nom_bdd);
            // Conectarse a la base de datos
            Statement statement = db_model.conexion.createStatement();

            // Ejecutar cada sentencia SQL del archivo
            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (!linea.isEmpty()) {
                    EjecutarSentenciaSQL(linea);
                }
            }

            // Cierro recursos
            statement.close();
            sc.close();
        } catch (FileNotFoundException | SQLException e) {
            System.err.println("Error al ejecutar el archivo SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void SeleccionarBaseDeDatos(String nombreBDD) {
        try (Statement statement = this.db_model.conexion.createStatement()) {
            statement.executeUpdate("USE " + nombreBDD);
        } catch (SQLException err) {
            System.err.println("Error al seleccionar la base de datos: " + err.getMessage());
            err.printStackTrace();
        }
    }
    
    private void EjecutarSentenciaSQL(String sentenciaSQL) {
        try {
            Statement statement = db_model.conexion.createStatement();
            statement.executeUpdate(sentenciaSQL);
            System.out.println("Sentencia SQL ejecutada correctamente: " + sentenciaSQL);
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la sentencia SQL: " + sentenciaSQL);
            e.printStackTrace();
        }
    }

    public Model getDb_model() {
        return db_model;
    }

    public void setDb_model(Model db_model) {
        this.db_model = db_model;
    }

    public String getRuta_arch() {
        return ruta_arch;
    }

    public void setRuta_arch(String ruta_arch) {
        this.ruta_arch = ruta_arch;
    }

    public String getNom_bdd() {
        return nom_bdd;
    }

    public void setNom_bdd(String nom_bdd) {
        this.nom_bdd = nom_bdd;
    }

}
