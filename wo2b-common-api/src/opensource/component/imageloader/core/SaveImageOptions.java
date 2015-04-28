package opensource.component.imageloader.core;

/**
 * 
 * @author 笨鸟不乖
 * 
 */
public class SaveImageOptions
{

	private String medule;

	private String title;
	
	private String extraDir;

	private SaveImageOptions(Builder builder)
	{
		this.medule = builder.medule;
		this.title = builder.title;
		this.extraDir = builder.extraDir;
	}

	public String getMedule()
	{
		return medule;
	}

	public String getTitle()
	{
		return title;
	}

	public String getExtraDir()
	{
		return extraDir;
	}

	/**
	 * Builder for {@link SaveImageOptions}
	 * 
	 */
	public static class Builder
	{

		private String medule;

		private String title;
		
		private String extraDir;

		public Builder medule(String medule)
		{
			this.medule = medule;
			return this;
		}
		
		public Builder title(String title)
		{
			this.title = title;
			return this;
		}

		public Builder extraDir(String extraDir)
		{
			this.extraDir = extraDir;
			return this;
		}

		/** Builds configured {@link SaveImageOptions} object */
		public SaveImageOptions build()
		{
			return new SaveImageOptions(this);
		}

	}

}
