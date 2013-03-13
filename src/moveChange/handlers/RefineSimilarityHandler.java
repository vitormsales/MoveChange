package moveChange.handlers;

import java.util.ArrayList;
import java.util.List;

import moveChange.Activator;
import moveChange.approach.AleatoryMoves;
import moveChange.ast.DeepDependencyVisitor;
import moveChange.basic.AllEntitiesMapping;
import moveChange.methods.AllMethods;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import dclsuite.util.DCLUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class RefineSimilarityHandler extends AbstractHandler {
	/**
	 * The constructor.
	 * 
	 */

	protected List<DeepDependencyVisitor> allDeepDependency;

	public RefineSimilarityHandler() {
		allDeepDependency = new ArrayList<DeepDependencyVisitor>();
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		try {

			IEditorPart editorPart = Activator.getDefault().getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();

			String activeProjectName;

			int numberOfClass=0;
			if (editorPart != null) {

				IFileEditorInput input = (IFileEditorInput) editorPart
						.getEditorInput();
				IFile file = input.getFile();
				IProject activeProject = file.getProject();
				activeProjectName = activeProject.getName();
				IProject project = ResourcesPlugin.getWorkspace().getRoot()
						.getProject(activeProjectName);
				IJavaProject javaProject = JavaCore.create(project);

				for (String className : DCLUtil.getClassNames(project)) {
					
					if (className == null) {
						continue;
					}


					IFile resource = DCLUtil.getFileFromClassName(javaProject,
							className);
					ICompilationUnit unit = ((ICompilationUnit) JavaCore
							.create((IFile) resource));
			
					System.out.println("AST para " + unit.getElementName());
					DeepDependencyVisitor deepDependency = new DeepDependencyVisitor(
							unit);
					this.allDeepDependency.add(deepDependency);
					numberOfClass++;
				}

				
				System.out.println(" inciando AllEntitiesMapping");
				AllEntitiesMapping.getInstance().createAllDependeciesMapping(
						allDeepDependency);
				System.out.println(" Terminou AllEntitiesMapping");

				System.out.println(" inciando AllMethods");
				AllMethods allMethods = new AllMethods(allDeepDependency);
				System.out.println(" Terminou AllMethods");
				AleatoryMoves aMoves = new AleatoryMoves(allMethods,numberOfClass);
				aMoves.changeProgram(activeProjectName);

				System.out.println("Fim");
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(window.getShell(), "Refine DialogBox",
				"Operação Finalizada com Sucesso");
		return null;
	}
}
