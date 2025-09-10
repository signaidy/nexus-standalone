package SpectraSystems.Nexus.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class DynamicTableNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    
    /** 
     * @param name
     * @param context
     * @return Identifier; muestra el nuevo identificador para usar al llamar la BD, para su propagacion
     */
    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        String projectName = "Nexus";
        return new Identifier(projectName.toUpperCase() + name.getText(), true);
    }
}