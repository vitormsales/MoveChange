package moveChange.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.corext.refactoring.structure.MoveInstanceMethodProcessor;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.ltk.core.refactoring.CheckConditionsOperation;
import org.eclipse.ltk.core.refactoring.CreateChangeOperation;
import org.eclipse.ltk.core.refactoring.PerformChangeOperation;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;

public class MoveMethod {

	protected static Refactoring getRefactoring(IMethod imethod,
			String candidate) throws OperationCanceledException, CoreException {

		MoveInstanceMethodProcessor processor = new MoveInstanceMethodProcessor(
				imethod,
				JavaPreferencesSettings.getCodeGenerationSettings(imethod
						.getJavaProject()));

		IProgressMonitor progressMonitor = new NullProgressMonitor();
		processor.checkInitialConditions(progressMonitor);

		IVariableBinding target = null;
		IVariableBinding[] targets = processor.getPossibleTargets();

		for (int i = 0; i < targets.length; i++) {
			IVariableBinding currentCandidat = targets[i];

			if (currentCandidat.getType().getQualifiedName().equals(candidate)) {
				target = currentCandidat;
				break;
			}
		}

		processor.setTarget(target);
		processor.setInlineDelegator(true);
		processor.setRemoveDelegator(true);
		processor.setDeprecateDelegates(false);


	

		Refactoring ref = new MoveRefactoring(processor);
		ref.checkInitialConditions(new NullProgressMonitor());

		return ref;
	}

	public static List<String> getpossibleRefactoring(IMethod method) {

		List<String> candidatesList = new ArrayList<String>();

		MoveInstanceMethodProcessor processor = new MoveInstanceMethodProcessor(
				method,
				JavaPreferencesSettings.getCodeGenerationSettings(method
						.getJavaProject()));
		IProgressMonitor progressMonitor = new NullProgressMonitor();

		try {
			processor.checkInitialConditions(progressMonitor);
		} catch (OperationCanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		IVariableBinding target = null;

		IVariableBinding[] targets = processor.getPossibleTargets();

		for (int i = 0; i < targets.length; i++) {
			IVariableBinding candidate = targets[i];
			if (!candidatesList
					.contains(candidate.getType().getQualifiedName())) {
				candidatesList.add(candidate.getType().getQualifiedName());
			}
		}
		return candidatesList;

	}

	public static boolean isValidCandidate(IMethod method, String candidate) {
		try {
			Refactoring refactoring;
			refactoring = getRefactoring(method, candidate);
			RefactoringStatus status = refactoring
					.checkAllConditions(new NullProgressMonitor());

			if (status.getSeverity() > RefactoringStatus.WARNING) {
				return false;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public static void executeMove(IMethod method, String candidate) {
		try {
			// TODO Auto-generated method stub
			Refactoring refactoring = getRefactoring(method, candidate);

			RefactoringStatus status = refactoring
					.checkAllConditions(new NullProgressMonitor());


			if (status.getSeverity() > RefactoringStatus.WARNING) {
				return;
			}

			final CreateChangeOperation create = new CreateChangeOperation(
					new CheckConditionsOperation(refactoring,
							CheckConditionsOperation.ALL_CONDITIONS),
					RefactoringStatus.FATAL);

			PerformChangeOperation perform = new PerformChangeOperation(create);

			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			// este comando aplica o refatoramento
			workspace.run(perform, new NullProgressMonitor());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}