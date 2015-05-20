package jasima_gui.uitest;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class TestGUI {
	private static SWTWorkbenchBot bot;
	private static SWTBotTreeItem projectItem;

	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		SWTBotPreferences.TIMEOUT = 30000L;
		bot.viewByTitle("Welcome").close();
		bot.menu("File").menu("New").menu("jasima Project").click();
		bot.shell("New Jasima Project").activate();
		bot.textWithLabel("&Project name:").setText("jasima_gui_test_001");
		bot.button("Finish").click();
		projectItem = bot.tree().getTreeItem("jasima_gui_test_001");
		projectItem.select();
		projectItem.expand();

		bot.menu("File").menu("New").menu("jasima Experiment").click();
		SWTBotShell newExp = bot.shell("New Jasima experiment");
		newExp.activate();
		bot.button("Finish").click();
		bot.waitUntil(Conditions.shellCloses(newExp));
	}

	@AfterClass
	public static void afterClass() {
		bot.sleep(1000L); 
	}

	@Test
	public void canRunExperiment() {
		projectItem.getNode("new_experiment.jasima").doubleClick();
		bot.toolbarButtonWithTooltip("Run Experiment").click();
		bot.waitUntil(new ICondition() {
			@Override
			public boolean test() throws Exception {
				for (SWTBotTreeItem item : projectItem.getItems()) {
					if (item.getText().startsWith("runResults_")) {
						item.contextMenu("Delete").click();
						bot.shell("Delete").activate();
						bot.button("OK").click();
						return true;
					}
				}
				return false;
			}

			@Override
			public void init(SWTBot bot) {
				// nothing
			}

			@Override
			public String getFailureMessage() {
				return "No run results found!";
			}
		});
	}

	@Test
	public void canUseCustomClass() {
		projectItem.select();
		bot.menu("File").menu("New").menu("Class").click();
		SWTBotShell newClass = bot.shell("New Java Class");
		newClass.activate();
		bot.textWithLabel("Na&me:").setText("TestPR");
		bot.textWithLabel("&Superclass:").setText("jasima.shopSim.core.PR");
		bot.button("Finish").click();
		bot.waitUntil(Conditions.shellCloses(newClass));
		
		projectItem.getNode("new_experiment.jasima").doubleClick();
		bot.toolbarButtonWithTooltip("New", 2).click();
		bot.shell("Types compatible with jasima.shopSim.core.PR").activate();
		bot.text().setText("TestPR");
		bot.waitUntil(Conditions.tableHasRows(bot.table(), 1));
		bot.button("OK").click();
		bot.activeEditor().save();
		canRunExperiment();
	}
}
