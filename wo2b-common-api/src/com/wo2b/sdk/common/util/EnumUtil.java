package com.wo2b.sdk.common.util;

import android.content.Intent;

public final class EnumUtil
{

	public static class Serializer<T extends Enum<T>> extends Deserializer<T>
	{

		private T victim;

		@SuppressWarnings("unchecked")
		public Serializer(T victim)
		{
			super((Class<T>) victim.getClass());
			this.victim = victim;
		}

		public void to(Intent intent)
		{
			intent.putExtra(name, victim.ordinal());
		}
	}

	public static class Deserializer<T extends Enum<T>>
	{

		protected Class<T> victimType;
		protected String name;

		public Deserializer(Class<T> victimType)
		{
			this.victimType = victimType;
			this.name = victimType.getName();
		}

		public T from(Intent intent)
		{
			if (!intent.hasExtra(name))
				throw new IllegalStateException();
			return victimType.getEnumConstants()[intent.getIntExtra(name, -1)];
		}
	}

	public static <T extends Enum<T>> Deserializer<T> deserialize(Class<T> victim)
	{
		return new Deserializer<T>(victim);
	}

	public static <T extends Enum<T>> Serializer<T> serialize(T victim)
	{
		return new Serializer<T>(victim);
	}
}