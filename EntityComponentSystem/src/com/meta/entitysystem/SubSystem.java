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
	
	private boolean check(UUID entity) {
		Boolean match = true;
		BitSet entityBits = globalEngine.getBitSet(entity);
		for (int index : systemBits) {
			if (!entityBits.get(index))
				match = false;
		}
		System.out.println(match);
		return match;
	}
	
	protected void add(UUID entity) {
		if (check(entity))
			entityList.add(entity);
	}
}
