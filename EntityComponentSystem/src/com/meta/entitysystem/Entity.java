package com.meta.entitysystem;

import java.util.UUID;

/**
 * Essentially a class dedicated to delegating things to the engine but set
 * so that is easily callable and readable within the code
 * @author Caleb Daniels
 *
 */
public class Entity {
	public static Engine engine;
	public UUID id;

	public Entity() {
		if (engine == null)
			throw new IllegalStateException("No engine set; Please create an engine;");
		id = engine.createEntity();
	}

	public void addComponent(Component component) { engine.addComponent(id, component); }

	public <T extends Component> T getComponent(Class<T> component) { 
		Component c = engine.getComponent(id, component);
		return (T) c;
	}

}
