package com.meta.entitysystem;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.UUID;

public abstract class SubSystem {
	public static Engine globalEngine;
	private ArrayList<Integer> systemBits;
	public  ArrayList<UUID> entityList;
	
	public SubSystem(Class<?>... componentCheck) {
		if (globalEngine == null)
			throw new IllegalStateException("No engine set; Please create an engine;");
		globalEngine.addSystem(this);
		systemBits = new ArrayList<Integer>();
		entityList = new ArrayList<UUID>();	
		
		for(Class<?> c : componentCheck)
			setBits(c);
	}
	
	/** Stub method to be overridden by the user  */
	protected abstract void process();
	
	private void setBits(Class<?> componentType) {
		systemBits.add(globalEngine.getIndex(componentType));
	}
	
	public boolean check(UUID entity) {
		Boolean match = true;
		BitSet entityBits = globalEngine.getBitSet(entity);
		for (int index : systemBits) {
			if (!entityBits.get(index))
				match = false;
		}
		if (match && !entityList.contains(entity)) {
			entityList.add(entity);
			add(entity);
		}
		else if (!match && entityList.contains(entity)) {
			entityList.remove(entity);
			remove(entity);
		}
		return match;
	}
	
	/**
	 *  This function is called everytime an entity is added 
	 * @param Entity that was added 
	 */
	protected abstract void add(UUID entity);
	

	/**
	 *  This function is called everytime an entity is removed 
	 * @param Entity that was removed
	 */
	protected abstract void remove(UUID entity);
}
