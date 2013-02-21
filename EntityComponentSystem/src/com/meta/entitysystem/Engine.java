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

	private ArrayList<UUID> addedEntities;



	public Engine() {
		entityList = new ArrayList<>();
		entityBits = new HashMap<>();
		entityGroups = new HashMap<>();
		componentMap = new HashMap<>();
		componentIndexes = new HashMap<>();
		systemsList = new ArrayList<>();

		addedEntities = new ArrayList<>();

		Entity.engine = this;
		SubSystem.globalEngine = this;
	}

	/** 
	 * Iterates through every system that has been added to the systems list and updates them
	 */
	public void update() {
		for (UUID entity : addedEntities) {
			addEntity(entity);
		}
		addedEntities.clear();

		for(SubSystem system : systemsList) {
			system.process();
		}
	}

	public UUID createEntity() {
		UUID id = UUID.randomUUID();
		entityList.add(id);
		addedEntities.add(id);
		entityBits.put(id, new BitSet());
		return id;
	}

	public void addComponent(UUID entity,Component component) {
		BitSet componentBits = entityBits.get(entity);
		componentBits.set(getIndex(component.getClass()));
		entityBits.put(entity, componentBits);

		HashMap<UUID, Component> componentList = componentMap.get(component.getClass());
		if (componentList == null) 
			componentList = new HashMap<>();
			componentList.put(entity, component);
			componentMap.put(component.getClass(), componentList);

	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(UUID entity, Class<T> componentType) {
		Component component = componentMap.get(componentType).get(entity);
		return (T) component;
	}

	/** Stub that may or may not be needed in the future */
	public BitSet getBitSet(UUID e) { 
		System.out.println(entityBits.get(e).toString());
		return entityBits.get(e);
	}

	@SuppressWarnings("unchecked")
	public int getIndex(Class<?>  componentType) {
		Integer index = componentIndexes.get(componentType);
		System.out.println(index);
		if (index == null)  {
			index = componentIndexes.size();
			componentIndexes.put((Class<? extends Component>) componentType, index); //TODO: watch the cast of this
		}
		return index;
	}

	public void addSystem(SubSystem subSystem) {
		systemsList.add(subSystem);
	}

	private void addEntity(UUID entity) {
		for (SubSystem system : systemsList)
			system.add(entity);
	}
}
