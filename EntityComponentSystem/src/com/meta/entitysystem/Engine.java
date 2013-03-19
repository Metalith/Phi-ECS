package com.meta.entitysystem;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class Engine {
	private ArrayList<UUID> entityList;
	private HashMap<UUID,String> entityNames;
	
	private HashMap<UUID, BitSet> entityBits; //TODO: Refactor this into seperate managers
	// Specifically a component and entity, with a possible system
	// With this engine class managing all of them 
	 //TODO: Possibly
//	private HashMap<String, ArrayList<UUID>> entityGroups;

	private HashMap<Class<? extends Component>, HashMap<UUID, Component>> componentMap;
	private HashMap<Class<? extends Component>, Integer> componentIndexes; //TODO: Change to ArrayList

	private LinkedList<SubSystem> systemsList;

	private ArrayList<UUID> addedEntities;



	public Engine() {
		entityList = new ArrayList<UUID>();
		entityBits = new HashMap<UUID, BitSet>();
		entityNames = new HashMap<UUID, String>();
		//TODO: add maybe
//		entityGroups = new HashMap<String, ArrayList<UUID>>();
		componentMap = new HashMap<Class<? extends Component>, HashMap<UUID, Component>>();
		componentIndexes = new HashMap<Class<? extends Component>, Integer>();
		systemsList = new LinkedList<SubSystem>();

		addedEntities = new ArrayList<UUID>();

		Entity.engine = this;
		SubSystem.globalEngine = this;
	}

	/** 
	 * Iterates through every system that has been added to the systems list and updates them
	 */
	public void update() {
		for (UUID entity : addedEntities) {
			for (SubSystem system : systemsList)
				system.add(entity);
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
	
	public UUID createEntity(String name) {
		UUID id = createEntity();
		entityNames.put(id, name);
		return id;
	}
	
	public String getName(UUID e) {
		return entityNames.get(e);
	}

	public void addComponent(UUID entity,Component component) {
		BitSet componentBits = entityBits.get(entity);
		componentBits.set(getIndex(component.getClass()));
		entityBits.put(entity, componentBits);

		HashMap<UUID, Component> componentList = componentMap.get(component.getClass());
		if (componentList == null) {
			componentList = new HashMap<UUID, Component>();
		}
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
}
