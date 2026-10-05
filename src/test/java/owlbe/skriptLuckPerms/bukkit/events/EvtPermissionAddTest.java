package owlbe.skriptLuckPerms.bukkit.events;

import ch.njol.skript.test.runner.SkriptJUnitTest;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.PermissionAddEvent;

public class EvtPermissionAddTest extends SkriptJUnitTest {

	static {
		setShutdownDelay(1);
	}

	@Test
	public void callEvent() {
		PermissionNode node = PermissionNode.builder("test-perm")
				.build();

		NodeAddEvent nodeEvent = Mockito.mock(NodeAddEvent.class);
		Mockito.when(nodeEvent.getNode()).thenReturn(node);

		PermissionAddEvent bukkitEvent = new PermissionAddEvent(nodeEvent);
		Bukkit.getPluginManager().callEvent(bukkitEvent);
	}

}


