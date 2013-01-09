package jasima_gui.wizards;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageTwo;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.ResolverConfiguration;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

public class JasimaNewProjectWizard extends Wizard implements INewWizard {

	private NewJavaProjectWizardPageOne mainPage;
	private NewJavaProjectWizardPageTwo lastPage;

	public JasimaNewProjectWizard() {
		mainPage = new NewJavaProjectWizardPageOne();
		lastPage = new NewJavaProjectWizardPageTwo(mainPage);
		setWindowTitle("New Jasima Project");
	}

	public void addPages() {
		addPage(mainPage);
		addPage(lastPage);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		IWorkbenchPart activePart = null;
		IWorkbenchWindow win = workbench.getActiveWorkbenchWindow();
		if (win != null) {
			IWorkbenchPage page = win.getActivePage();
			if (page != null) {
				activePart = page.getActivePart();
			}
		}
		mainPage.init(selection, activePart);
	}

	public boolean canFinish() {
		return mainPage.canFlipToNextPage();
	}

	public boolean performFinish() {
		try {
			lastPage.performFinish(new NullProgressMonitor());
			IJavaProject proj = lastPage.getJavaProject();
			String line;
			BufferedReader rdr = new BufferedReader(
					new InputStreamReader(JasimaNewProjectWizard.class
							.getResourceAsStream("default_pom.xml"), "utf8"));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			OutputStreamWriter wrtr = new OutputStreamWriter(bos, "utf8");
			while ((line = rdr.readLine()) != null) {
				wrtr.append(line.replace("${PROJECT_NAME}", proj.getProject()
						.getName()));
				wrtr.append('\n');
			}
			wrtr.flush();
			proj.getProject()
					.getFile("pom.xml")
					.create(new ByteArrayInputStream(bos.toByteArray()), true,
							new NullProgressMonitor());
			MavenPlugin.getProjectConfigurationManager().enableMavenNature(
					proj.getProject(), new ResolverConfiguration(),
					new NullProgressMonitor());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean performCancel() {
		lastPage.performCancel();
		return super.performCancel();
	}

}
