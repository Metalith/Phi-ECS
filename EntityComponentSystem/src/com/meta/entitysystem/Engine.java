package com.meta.entitysystem;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.UUID;

public class Engine {
	private ArrayList<UUID> entityList;
	private HashMap<UUID, BitSet> entityBits; //TODO: Refactor this into seperate managers
	// Specifically a component and entity, with a possible system
	// With this engine class managing all of them 
	private HashMap<String, ArrayList<UUID>> entityGroups;

	private HashMap<Class<? extends Component>, HashMap<UUID, Component>> componentMap;
	private HashMap<Class<? extends Component>, Integer> componentIndexes; //TODO: Change to ArrayList

	private ArrayList<SubSystem> systemsList;



	public Engine() {
		entityList = new ArrayList<>();
		entityBits = new HashMap<>();
		entityGroups = new HashMap<>();
		componentMap = new HashMap<>();
		componentIndexes = new HashMap<>();
		systemsList = new ArrayList<>();

		Entity.engine = this;
	}

	/** 
	 * Iterates through every system that has been added to the systems list and updates them
	 */
	public void update() {
		for(SubSystem system : systemsList) {
			system.process();
		}
	}

	public UUID createEntity() {
		UUID id = UUID.randomUUID();
		entityList.add(id);
		return id;
	}

	public void addComponent(UUID entity,Component component) {
		BitSet componentBits = entityBits.get(entity);
		if (componentBits == null) {
			componentBits = new BitSet();
		}
		componentBits.set(getIndex(component.getClass()));
		entityBits.put(entity, componentBits);

		HashMap<UUID, Component> componentList = componentMap.get(component.getClass());
		if (componentList == null) 
			componentList = new HashMap<>();
		componentList.put(entity, component);
		componentMap.put(component.getClass(), componentList);

	}
	
	public <T extends Component> T getComponent(UUID entity, Class<T> componentType) {
		Component component = componentMap.get(componentType).get(entity);
		return (T) component;
	}

	private int getIndex(Class<? extends Component>  component) {
		Integer index = componentIndexes.get(component);
		if (index == null)  {
			componentIndexes.put(component, index);
			index = componentIndexes.size();
		}

		return index;
	}
}
