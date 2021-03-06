package Views;

import Views.Widget.*;
import algorithms.search.Solution;
import mazeGenerators.Maze3d;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import java.util.*;
import java.util.List;

import static java.lang.System.exit;

public class MazeWindow<T> extends BasicWindow {

	private MazeDisplay mazeDisplay;
	TimerTask task;
	Timer timer;
	BasicWindow b = this;
	List<DialogWindow> dView = new ArrayList<>();
	String mazeNameMazeWindow;
	Button btnDisplaySolution;
	Button saveMaze,btnSolveMaze,loadXML;



	@Override
	protected void initWidgets() {

		GridLayout grid = new GridLayout(2, false);
		shell.setLayout(grid);

		Composite buttons = new Composite(shell, SWT.NONE);
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		buttons.setLayout(rowLayout);

		Button btnGenerateMaze = new Button(buttons, SWT.PUSH);
		btnGenerateMaze.setText("Generate maze");
		btnGenerateMaze.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				DialogWindow win = new GenerateMazeWindow();
				win.addObserver(b);
				win.start(display);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {


			}
		});

		Button loadMaze = new Button(buttons, SWT.PUSH);
		loadMaze.setText("Load maze");
		loadMaze.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						GenerateLoadSaveWindow win=new GenerateLoadSaveWindow(true,mazeDisplay.mazeName);
						win.addObserver(b);
						win.start(display);
						shell.open();
				}
				});
				mazeDisplay.setFocus();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		loadXML = new Button(buttons, SWT.PUSH);
		loadXML.setText("Open properties");
		loadXML.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						GenerateLoadXmlWindow win=new GenerateLoadXmlWindow();
						win.addObserver(b);
						win.start(display);
					}
				});
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		saveMaze = new Button(buttons, SWT.PUSH);
		saveMaze.setText("Save maze");
		saveMaze.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						GenerateLoadSaveWindow win=new GenerateLoadSaveWindow(false,mazeDisplay.mazeName);
						win.addObserver(b);
						win.start(display);
					}
				});
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		btnSolveMaze = new Button(buttons, SWT.PUSH);
		btnSolveMaze.setText("Solve maze");
		btnSolveMaze.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						DialogWindow win = new GenerateSolveMaze(mazeDisplay.mazeName);
						win.addObserver(b);
						win.start(display);
						dView.add(win);
					}
				});


			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		btnDisplaySolution = new Button(buttons, SWT.PUSH);
		btnDisplaySolution.setText("Display Solution");
		btnDisplaySolution.addSelectionListener(new SelectionListener()	{
			@Override
			public void widgetSelected(SelectionEvent e) {


						display.asyncExec(new Runnable() {

							@Override
							public void run() {
						mazeDisplay.solve();


							}
						});

					}


			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}
		});

		Button bExit = new Button(buttons, SWT.PUSH);
		bExit.setText("Exit");
		bExit.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				DialogWindow win = new GenerateExitWindow();
				win.addObserver(b);
				win.start(display);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		shell.addListener(SWT.Close, new Listener()
		{
			public void handleEvent(Event event)
			{
				display.asyncExec(new Runnable() {
						@Override
						public void run() {
							mazeDisplay.task.cancel();
							task.cancel();
							exit(1);

						}
					});


			}
		});

		run();
		mazeDisplay = new MazeDisplay(shell, SWT.BORDER|SWT.DOUBLE_BUFFERED);
		mazeDisplay.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		mazeDisplay.setFocus();

	}
	@Override
	public int getUserCommand() {
		return 0;
	}
	@Override
	public void display(String str) {
		if (str.split(" ")[0].equals("Maze:"))mazeNameMazeWindow=str.split(" ")[1];
		if (str.length()<50)
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					GenerateNoteWindow.getInstance(str, display);
					mazeDisplay.setFocus();
				}
			});

				}
	@Override
	public void display(Object tValue) {

		if (tValue.getClass().getName() == "java.lang.String") {
			display((String) tValue);
			return;
		}
		if (tValue.getClass().getName() == "mazeGenerators.Maze3d") {
			mazeDisplay.setMyMaze((Maze3d) tValue);
			mazeDisplay.mazeName = this.mazeNameMazeWindow;

			return;
		}
		if (tValue.getClass().getName() == "algorithms.search.Solution") {
			display.asyncExec(new Runnable() {

				@Override
				public void run() {
					mazeDisplay.setSolution((Solution)tValue);
					mazeDisplay.solutionAvailable=true;

				}
			});

		}
	}
	@Override
	public void display(int[][] maze3d) {
	}
	@Override
	public void setCli(Cli c) {
	}
	@Override
	public void update(Observable o, Object arg) {
		String[] strSplit = o.getClass().getName().split("\\.");
		String s=(String)arg;
		String[] str = s.split(" ");
		switch (strSplit[0])
		{
				case "Views":
					{
					if (str[0].equals("generate_maze")) {
						mazeNameMazeWindow = new String(str[1]);
						setChanged();
						notifyObservers(str);
						break;
					}
						/*if (str[0].equals("load_maze")) {
							mazeNameMazeWindow = new String(str[1]);
							setChanged();
							notifyObservers(str);
							break;
						}
						*/
					else {
						setChanged();
						notifyObservers(str);
						break;
					}


				}
				case "java.lang.String": {
					setChanged();
					notifyObservers(str);
					break;
				}

		}
			


	}
	@Override
	public void run() {

		task = new TimerTask() {

			@Override
			public void run() {
				display.asyncExec(new Runnable() {

					@Override
					public void run() {
				if(mazeDisplay.mazeName!=null)
					{
						if (mazeDisplay.status){
							saveMaze.setEnabled(mazeDisplay.activeMaze);
							btnDisplaySolution.setEnabled(mazeDisplay.solutionAvailable && mazeDisplay.mazeName.equals(mazeNameMazeWindow));
							btnSolveMaze.setEnabled(mazeDisplay.activeMaze);

						}
					}
					}
				});

			}
		};
		timer = new Timer();
		timer.scheduleAtFixedRate(task, 0,2000);


	}
	public void display(Maze3d maze3d) {
		mazeDisplay.setMyMaze( maze3d);
		mazeDisplay.mazeName = this.mazeNameMazeWindow;
		mazeDisplay.setFocus();

	}

}


