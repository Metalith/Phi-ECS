package com.meta.entitysystem;

import java.util.UUID;

/**
 * Essentially a class dedicated to delegating things to the engine but set
 * so that is easily callable and readable within the code
 * @author Pegasus
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
	
}
