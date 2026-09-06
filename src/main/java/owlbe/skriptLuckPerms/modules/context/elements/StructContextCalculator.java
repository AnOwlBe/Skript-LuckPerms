package owlbe.skriptLuckPerms.modules.context.elements;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.lang.util.SimpleEvent;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.SectionEntryData;
import org.skriptlang.skript.lang.structure.Structure;
import org.skriptlang.skript.log.runtime.ErrorSource;
import org.skriptlang.skript.log.runtime.RuntimeErrorProducer;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Register Context Calculator")
@Description("""
	Registers a LuckPerms context calculator with the given key and trigger.
	
	Avoid putting code in the trigger that will call the calculator (trigger) again, \
	such as the broadcast effect.
	""")
@Example("""
	register a luckperms context calculator:
	    key: "prefix"
	    trigger:
	        set {_lp} to quick luckperms user from player
	        set {_prefix} to luckperms prefix of {_lp}
	        return value of {_prefix}
	""")

@Since("INSERT VERSION")
public class StructContextCalculator extends Structure implements ReturnHandler<String>, RuntimeErrorProducer {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.STRUCTURE,
				SyntaxInfo.Structure.builder(StructContextCalculator.class)
						.addPattern("register [a] [new] luckperm[s] context calculator")
						.entryValidator(EntryValidator.builder()
								.addEntry("key", "customcontext", false)
								.addEntryData(new SectionEntryData("trigger", null, false))
								.build())
						.build()
		);
	}

	private @Nullable EntryContainer entryContainer;
	private @Nullable ContextCalculator<Player> calculator;
	private static final ThreadLocal<Boolean> IN_CALCULATION = ThreadLocal.withInitial(() -> false);

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult, @Nullable EntryContainer entryContainer) {
		this.entryContainer = entryContainer;
		return true;
	}

	@Override
	public boolean load() {
		if (entryContainer == null)
			return false;

		ParserInstance parser = getParser();
		String key = entryContainer.get("key", String.class, true);
		SectionNode node = entryContainer.get("trigger", SectionNode.class, false);

		if (node != null) {
			parser.setCurrentEvent("context calculator trigger", ContextCalculatorEvent.class);

			ReturnableTrigger<String> trigger = loadReturnableTrigger(
					node,
					"context calculator trigger",
					new SimpleEvent()
			);

			parser.deleteCurrentEvent();

			calculator = (target, contextConsumer) -> {
				if (IN_CALCULATION.get()) {
					error("Infinite loop detected. Please remove anything in the trigger that calls the calculator again (such as the broadcast effect).");
					return;
				}

				// This system is used to prevent a deadlock on the server if you e.g.
				// put a broadcast inside the trigger (calling the context check again)
				// Though it'll still spam the console and isn't something to be ignored
				IN_CALCULATION.set(true);

				try {
					ContextCalculatorEvent event = new ContextCalculatorEvent(target, key, contextConsumer);

					TriggerItem.walk(trigger, event);
					if (event.result != null)
						contextConsumer.accept(key, event.result);
				} finally {
					IN_CALCULATION.set(false);
				}
			};

			LuckPermsProvider.get().getContextManager().registerCalculator(calculator);
		}

		return true;
	}

	@Override
	public void unload() {
		if (calculator != null)
			LuckPermsProvider.get().getContextManager().unregisterCalculator(calculator);
	}

	@Override
	public void returnValues(Event event, Expression<? extends String> value) {
		if (!(event instanceof ContextCalculatorEvent contextCalculatorEvent))
			return;

		contextCalculatorEvent.result = value.getSingle(event);
	}

	@Override
	public boolean isSingleReturnValue() {
		return true;
	}

	@Override
	public @NotNull Class<? extends String> returnValueType() {
		return String.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "register luckperms context calculator";
	}

	@Override
	public @NotNull ErrorSource getErrorSource() {
		SectionNode node = entryContainer != null ? entryContainer.getSource() : null;
		return ErrorSource.fromNodeAndElement(node, this);
	}

	public static class ContextCalculatorEvent extends PlayerEvent {

		public String key;
		public ContextConsumer consumer;
		public @Nullable String result;

		public ContextCalculatorEvent(Player player, String key, ContextConsumer consumer) {
			super(player);
			this.key = key;
			this.consumer = consumer;
		}

		@Override
		public @NotNull HandlerList getHandlers() {
			throw new UnsupportedOperationException();}
	}

}
