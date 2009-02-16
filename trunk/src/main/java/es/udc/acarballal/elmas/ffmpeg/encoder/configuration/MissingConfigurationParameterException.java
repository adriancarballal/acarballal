package es.udc.acarballal.elmas.ffmpeg.encoder.configuration;

/**
 * 
 * @author Adrian Carballal Mato
 * @version 0.1 29/10/2008
 * 
 */
public class MissingConfigurationParameterException extends Exception {

    private String parameterName;

    /**
     * Constructor used to create this object.
     * @param parameterName Indicates the missing parameter.
     */
    public MissingConfigurationParameterException(String parameterName) {
        super("Missing configuration parameter: '" + parameterName + "'");
        this.parameterName = parameterName;
    }
    
    /**
     * Returns the parameter asociated with the parameter name this 
     * object was created.
     * @return the missing parameter name.
     */
    public String getParameterName() {
        return parameterName;
    }

	
}
