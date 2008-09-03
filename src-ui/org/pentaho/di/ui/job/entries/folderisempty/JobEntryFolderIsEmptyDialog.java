 /**********************************************************************
 **                                                                   **
 **               This code belongs to the KETTLE project.            **
 **                                                                   **
 **                                                                   **
 **                                                                   **
 **********************************************************************/


package org.pentaho.di.ui.job.entries.folderisempty;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.pentaho.di.core.Const;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.folderisempty.JobEntryFolderIsEmpty;
import org.pentaho.di.job.entries.folderisempty.Messages;
import org.pentaho.di.job.entry.JobEntryDialogInterface;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.core.gui.WindowProperty;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.job.entry.JobEntryDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;


/**
 * This dialog allows you to edit the Create Folder job entry settings.
 *
 * @author Sven/Samatar
 * @since  17-10-2007
 */
public class JobEntryFolderIsEmptyDialog extends JobEntryDialog implements JobEntryDialogInterface
{
	
	private Label        wlName;
	private Text         wName;
    private FormData     fdlName, fdName;

	private Label        wlFoldername;
	private Button       wbFoldername;
	private TextVar      wFoldername;
	private FormData     fdlFoldername, fdbFoldername, fdFoldername;
	
    private Label        wlIncludeSubFolders;
    private Button       wIncludeSubFolders;
    private FormData     fdlIncludeSubFolders, fdIncludeSubFolders;
	
    private Label        wlSpecifyWildcard;
    private Button       wSpecifyWildcard;
    private FormData     fdlSpecifyWildcard, fdSpecifyWildcard;
    
	private Label        wlWildcard;
	private TextVar      wWildcard;
	private FormData     fdlWildcard, fdWildcard;    

	private Button wOK, wCancel;
	private Listener lsOK, lsCancel;

	private JobEntryFolderIsEmpty jobEntry;
	private Shell       	shell;

	private SelectionAdapter lsDef;

	private boolean changed;


 	public JobEntryFolderIsEmptyDialog(Shell parent, JobEntryInterface jobEntryInt, Repository rep,
				JobMeta jobMeta)
	{
		super(parent, jobEntryInt, rep, jobMeta);
		jobEntry = (JobEntryFolderIsEmpty) jobEntryInt;
		if (this.jobEntry.getName() == null)
			this.jobEntry.setName(Messages.getString("JobFolderIsEmpty.Name.Default")); //$NON-NLS-1$
	}

	public JobEntryInterface open()
	{
		Shell parent = getParent();
		Display display = parent.getDisplay();

        shell = new Shell(parent, props.getJobsDialogStyle());
        props.setLook(shell);
        JobDialog.setShellImage(shell, jobEntry);

		ModifyListener lsMod = new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				jobEntry.setChanged();
			}
		};
		changed = jobEntry.hasChanged();

		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth  = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;

		shell.setLayout(formLayout);
		shell.setText(Messages.getString("JobFolderIsEmpty.Title"));

		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;

		// Foldername line
		wlName=new Label(shell, SWT.RIGHT);
		wlName.setText(Messages.getString("JobFolderIsEmpty.Name.Label"));
 		props.setLook(wlName);
		fdlName=new FormData();
		fdlName.left = new FormAttachment(0, 0);
		fdlName.right= new FormAttachment(middle, -margin);
		fdlName.top  = new FormAttachment(0, margin);
		wlName.setLayoutData(fdlName);
		wName=new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
 		props.setLook(wName);
		wName.addModifyListener(lsMod);
		fdName=new FormData();
		fdName.left = new FormAttachment(middle, 0);
		fdName.top  = new FormAttachment(0, margin);
		fdName.right= new FormAttachment(100, 0);
		wName.setLayoutData(fdName);

		// Foldername line
		wlFoldername=new Label(shell, SWT.RIGHT);
		wlFoldername.setText(Messages.getString("JobFolderIsEmpty.Foldername.Label"));
 		props.setLook(wlFoldername);
		fdlFoldername=new FormData();
		fdlFoldername.left = new FormAttachment(0, 0);
		fdlFoldername.top  = new FormAttachment(wName, margin);
		fdlFoldername.right= new FormAttachment(middle, -margin);
		wlFoldername.setLayoutData(fdlFoldername);

		wbFoldername=new Button(shell, SWT.PUSH| SWT.CENTER);
 		props.setLook(wbFoldername);
		wbFoldername.setText(Messages.getString("System.Button.Browse"));
		fdbFoldername=new FormData();
		fdbFoldername.right= new FormAttachment(100, 0);
		fdbFoldername.top  = new FormAttachment(wName, 0);
		wbFoldername.setLayoutData(fdbFoldername);

		wFoldername=new TextVar(jobMeta,shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
 		props.setLook(wFoldername);
		wFoldername.addModifyListener(lsMod);
		fdFoldername=new FormData();
		fdFoldername.left = new FormAttachment(middle, 0);
		fdFoldername.top  = new FormAttachment(wName, margin);
		fdFoldername.right= new FormAttachment(wbFoldername, -margin);
		wFoldername.setLayoutData(fdFoldername);
		
		// Include sub folders?
        wlIncludeSubFolders = new Label(shell, SWT.RIGHT);
        wlIncludeSubFolders.setText(Messages.getString("JobFolderIsEmpty.IncludeSubFolders.Label"));
        props.setLook(wlIncludeSubFolders);
        fdlIncludeSubFolders = new FormData();
        fdlIncludeSubFolders.left = new FormAttachment(0, 0);
        fdlIncludeSubFolders.top = new FormAttachment(wFoldername, margin);
        fdlIncludeSubFolders.right = new FormAttachment(middle, -margin);
        wlIncludeSubFolders.setLayoutData(fdlIncludeSubFolders);
        wIncludeSubFolders = new Button(shell, SWT.CHECK);
        props.setLook(wIncludeSubFolders);
        wIncludeSubFolders.setToolTipText(Messages.getString("JobFolderIsEmpty.IncludeSubFolders.Tooltip"));
        fdIncludeSubFolders = new FormData();
        fdIncludeSubFolders.left = new FormAttachment(middle, 0);
        fdIncludeSubFolders.top = new FormAttachment(wFoldername, margin);
        fdIncludeSubFolders.right = new FormAttachment(100, 0);
        wIncludeSubFolders.setLayoutData(fdIncludeSubFolders);
        wIncludeSubFolders.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                jobEntry.setChanged();
            }
        });
	
        // Specify wildcard?
        wlSpecifyWildcard = new Label(shell, SWT.RIGHT);
        wlSpecifyWildcard.setText(Messages.getString("JobFolderIsEmpty.SpecifyWildcard.Label"));
        props.setLook(wlSpecifyWildcard);
        fdlSpecifyWildcard = new FormData();
        fdlSpecifyWildcard.left = new FormAttachment(0, 0);
        fdlSpecifyWildcard.top = new FormAttachment(wIncludeSubFolders, margin);
        fdlSpecifyWildcard.right = new FormAttachment(middle, -margin);
        wlSpecifyWildcard.setLayoutData(fdlSpecifyWildcard);
        wSpecifyWildcard = new Button(shell, SWT.CHECK);
        props.setLook(wSpecifyWildcard);
        wSpecifyWildcard.setToolTipText(Messages.getString("JobFolderIsEmpty.SpecifyWildcard.Tooltip"));
        fdSpecifyWildcard = new FormData();
        fdSpecifyWildcard.left = new FormAttachment(middle, 0);
        fdSpecifyWildcard.top = new FormAttachment(wIncludeSubFolders, margin);
        fdSpecifyWildcard.right = new FormAttachment(100, 0);
        wSpecifyWildcard.setLayoutData(fdSpecifyWildcard);
        wSpecifyWildcard.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                jobEntry.setChanged();
                CheckLimitSearch();
            }
        });
        

        
		// Wildcard line
		wlWildcard=new Label(shell, SWT.RIGHT);
		wlWildcard.setText(Messages.getString("JobFolderIsEmpty.Wildcard.Label"));
 		props.setLook(wlWildcard);
		fdlWildcard=new FormData();
		fdlWildcard.left = new FormAttachment(0, 0);
		fdlWildcard.top  = new FormAttachment(wSpecifyWildcard, margin);
		fdlWildcard.right= new FormAttachment(middle, -margin);
		wlWildcard.setLayoutData(fdlWildcard);
		wWildcard=new TextVar(jobMeta,shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
 		props.setLook(wWildcard);
		wWildcard.addModifyListener(lsMod);
		fdWildcard=new FormData();
		fdWildcard.left = new FormAttachment(middle, 0);
		fdWildcard.top  = new FormAttachment(wSpecifyWildcard, margin);
		fdWildcard.right= new FormAttachment(100, -margin);
		wWildcard.setLayoutData(fdWildcard);
		
		// Whenever something changes, set the tooltip to the expanded version:
		wFoldername.addModifyListener(new ModifyListener()
			{
				public void modifyText(ModifyEvent e)
				{
					wFoldername.setToolTipText(jobMeta.environmentSubstitute( wFoldername.getText() ) );
				}
			}
		);
		// Whenever something changes, set the tooltip to the expanded version:
		wWildcard.addModifyListener(new ModifyListener()
			{
				public void modifyText(ModifyEvent e)
				{
					wWildcard.setToolTipText(jobMeta.environmentSubstitute( wWildcard.getText() ) );
				}
			}
		);

		wbFoldername.addSelectionListener
		(
			new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent e)
				{
					DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
					if (wFoldername.getText()!=null)
					{
						dialog.setFilterPath(jobMeta.environmentSubstitute(wFoldername.getText()) );
					}
				
					String dir=dialog.open();
					if(dir!=null)
					{
						wFoldername.setText(dir);
					}
				}
			}
		);

      
		
        wOK = new Button(shell, SWT.PUSH);
        wOK.setText(Messages.getString("System.Button.OK"));
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText(Messages.getString("System.Button.Cancel"));
        
		BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel }, margin, wWildcard);

		// Add listeners
		lsCancel   = new Listener() { public void handleEvent(Event e) { cancel(); } };
		lsOK       = new Listener() { public void handleEvent(Event e) { ok();     } };

		wCancel.addListener(SWT.Selection, lsCancel);
		wOK.addListener    (SWT.Selection, lsOK    );

		lsDef=new SelectionAdapter() { public void widgetDefaultSelected(SelectionEvent e) { ok(); } };

		wName.addSelectionListener( lsDef );
		wFoldername.addSelectionListener( lsDef );

		// Detect X or ALT-F4 or something that kills this window...
		shell.addShellListener(	new ShellAdapter() { public void shellClosed(ShellEvent e) { cancel(); } } );

		getData();
		CheckLimitSearch();

		BaseStepDialog.setSize(shell);

		shell.open();
		while (!shell.isDisposed())
		{
				if (!display.readAndDispatch()) display.sleep();
		}
		return jobEntry;
	}

	public void dispose()
	{
		WindowProperty winprop = new WindowProperty(shell);
		props.setScreen(winprop);
		shell.dispose();
	}
	private void CheckLimitSearch()
	{
		wlWildcard.setEnabled(wSpecifyWildcard.getSelection());
		wWildcard.setEnabled(wSpecifyWildcard.getSelection());
	}

	/**
	 * Copy information from the meta-data input to the dialog fields.
	 */
	public void getData()
	{
		if (jobEntry.getName()!= null) wName.setText( jobEntry.getName() );
		wName.selectAll();
		if (jobEntry.getFoldername()!= null) wFoldername.setText( jobEntry.getFoldername() );
		wIncludeSubFolders.setSelection(jobEntry.isIncludeSubFolders());
		wSpecifyWildcard.setSelection(jobEntry.isSpecifyWildcard());
		if (jobEntry.getWildcard()!= null) wWildcard.setText( jobEntry.getWildcard() );
		
		
	}

	private void cancel()
	{
		jobEntry.setChanged(changed);
		jobEntry=null;
		dispose();
	}

	private void ok()
	{
		jobEntry.setName(wName.getText());
		jobEntry.setFoldername(wFoldername.getText());
		jobEntry.setIncludeSubFolders(wIncludeSubFolders.getSelection());
		jobEntry.setSpecifyWildcard(wSpecifyWildcard.getSelection());
		jobEntry.setWildcard(wWildcard.getText());
		
		dispose();
	}

	public String toString()
	{
		return this.getClass().getName();
	}

	public boolean evaluates()
	{
		return true;
	}

	public boolean isUnconditional()
	{
		return false;
	}
}