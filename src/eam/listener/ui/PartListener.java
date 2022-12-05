package eam.listener.ui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.Charsets;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import eam.listener.edit.CodeAssistListener;
import eam.listener.edit.KeyEventListener;
import eam.utils.date.CurrentDate;
import eam.utils.http.UploadData;

public class PartListener implements IPartListener {
	IWorkbench workbench = PlatformUI.getWorkbench();

	KeyListener keyListener = new KeyEventListener();
	CodeAssistListener codeAssistListener = new CodeAssistListener();

	UploadData request = new UploadData();
	CurrentDate date = new CurrentDate();
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String startDate = (new Date()).toString();
	public static String endDate;

	public PartListener(String startDate) {
		// TODO Auto-generated constructor stub
		PartListener.startDate = startDate;
	}

	@Override
	public void partActivated(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub
		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage workbenchPage;
		String name = arg0.getTitle();
		workbenchPage = workbenchWindow.getPages()[0];
		Pattern mpattern = Pattern.compile("\\.(java|jsp|xml|js|json|css|html|class|properties|sql|md)",
				Pattern.CASE_INSENSITIVE);
		Matcher mmatcher = mpattern.matcher(name);
		if (mmatcher.find()) {
			PartListener.startDate = date.getDate();
		}
		// System.out.println(new Date() + arg0.getTitle() + ": 组件激活");

		IEditorPart editorPart = workbenchPage.getActiveEditor();
		try {
			if (editorPart != null) {
				editorPart.getAdapter(org.eclipse.swt.widgets.Control.class)
						.addKeyListener((org.eclipse.swt.events.KeyListener) keyListener);
			}
		} catch (Exception e) {
			// TODO: handle exception

		}

		// SourceViewer sourceViewer = (SourceViewer)
		// editorPart.getAdapter(ITextOperationTarget.class);
		// sourceViewer.getContentAssistantFacade().addCompletionListener(codeAssistListener);
		System.out.println("--------partActivated结束------------");

	}

	@Override
	public void partBroughtToTop(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub
		// System.out.println(new Date() + arg0.getTitle() +": 组件置顶");
	}

	@Override
	public void partClosed(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub
		 System.out.println(new Date() + arg0.getTitle() +": 啦啦啦啦 组件关闭");
	}

	@Override
	public void partDeactivated(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub
		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage workbenchPage;
		workbenchPage = workbenchWindow.getPages()[0];
		PartListener.endDate = date.getDate();
		String name = arg0.getTitle();
		// System.out.println(new Date() + arg0.getTitle() +": 组件取消激活");
		IEditorPart editorPart = workbenchPage.getActiveEditor();
		try {
			if (editorPart != null) {
				editorPart.getAdapter(org.eclipse.swt.widgets.Control.class)
						.removeKeyListener((org.eclipse.swt.events.KeyListener) keyListener);
				IEditorInput input = editorPart.getEditorInput();
				IFile file = (IFile) input.getAdapter(IFile.class);
				 if (file != null) {
				    // do stuff
					 InputStream is = file.getContents();
					 StringBuilder sb = new StringBuilder();
						String line;

						BufferedReader br = new BufferedReader(new InputStreamReader(is));
						while ((line = br.readLine()) != null) {
						    sb.append(line);
						}
						
						String str = sb.toString();
						System.out.println("输出"+str );
					 
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// SourceViewer sourceViewer = (SourceViewer)
		// editorPart.getAdapter(ITextOperationTarget.class);
		// sourceViewer.getContentAssistantFacade().removeCompletionListener(codeAssistListener);

		Pattern mpattern = Pattern.compile("\\.(java|jsp|xml|js|json|css|html|class|properties|sql|md)",
				Pattern.CASE_INSENSITIVE);
		Matcher mmatcher = mpattern.matcher(name);
		if (mmatcher.find()) {
			if (PartListener.startDate != null) {
				try {
					Date dd1 = df.parse(PartListener.startDate);
					Date dd2 = df.parse(PartListener.endDate);
					Integer second = (int) ((dd2.getTime() - dd1.getTime()) / 1000);
					if (second > 1)
						// 编辑文件，起止时间，持续秒数
						request.doPost("Edit-EditFile", name,
								PartListener.startDate + " - " + PartListener.endDate + " : " + second + "s");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("----------partDeactivated结束------------");
	}

	@Override
	public void partOpened(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub
	    System.out.println(new Date() + arg0.getTitle() +":蹦蹦蹦  组件开启");
	}

}
