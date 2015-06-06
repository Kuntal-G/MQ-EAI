package com.general.mq.common.util.conf;

public class ErrorConfig {
	
	private static final ConfigManager instance = ConfigManager.instance();
	private static final String ERROR_CONFIGURATION_XML = "error-configuration.xml";
	
	//System Error Code Related
	public static final String CODE_101= instance.getProperty("code.101",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_102= instance.getProperty("code.102",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_103= instance.getProperty("code.103",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_104= instance.getProperty("code.104",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_105= instance.getProperty("code.105",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_106= instance.getProperty("code.106",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_107= instance.getProperty("code.107",ErrorConfig.ERROR_CONFIGURATION_XML);
	
		
	//Application Error Code Related
	public static final String CODE_201= instance.getProperty("code.201",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_202= instance.getProperty("code.202",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_203= instance.getProperty("code.203",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_204= instance.getProperty("code.204",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_205= instance.getProperty("code.205",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_206= instance.getProperty("code.206",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_207= instance.getProperty("code.207",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_208= instance.getProperty("code.208",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_209= instance.getProperty("code.209",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_210= instance.getProperty("code.210",ErrorConfig.ERROR_CONFIGURATION_XML);
	
	
	//Validation Error Code Related
	public static final String CODE_301= instance.getProperty("code.301",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_302= instance.getProperty("code.302",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_303= instance.getProperty("code.303",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_304= instance.getProperty("code.304",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_305= instance.getProperty("code.305",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_306= instance.getProperty("code.306",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_307= instance.getProperty("code.307",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_308= instance.getProperty("code.308",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_309= instance.getProperty("code.309",ErrorConfig.ERROR_CONFIGURATION_XML);

	
	
	//Generic Error Code Related
	public static final String CODE_404= instance.getProperty("code.404",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_401= instance.getProperty("code.401",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_400= instance.getProperty("code.400",ErrorConfig.ERROR_CONFIGURATION_XML);
	public static final String CODE_500= instance.getProperty("code.500",ErrorConfig.ERROR_CONFIGURATION_XML);

		
	
}
