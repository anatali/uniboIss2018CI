/* Generated by AN DISI Unibo */ 
package it.unibo.caller;
import it.unibo.qactors.PlanRepeat;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.StateExecMessage;
import it.unibo.qactors.QActorUtils;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.action.AsynchActionResult;
import it.unibo.qactors.action.IActorAction;
import it.unibo.qactors.action.IActorAction.ActionExecMode;
import it.unibo.qactors.action.IMsgQueue;
import it.unibo.qactors.akka.QActor;
import it.unibo.qactors.StateFun;
import java.util.Stack;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.qactors.action.ActorTimedAction;
public abstract class AbstractCaller extends QActor { 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	//protected String mqttServer = "";
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractCaller(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/caller/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/caller/plans.txt";
	  	}
		@Override
		protected void doJob() throws Exception {
			String name  = getName().replace("_ctrl", "");
			mysupport = (IMsgQueue) QActorUtils.getQActor( name ); 
			initStateTable(); 
	 		initSensorSystem();
	 		history.push(stateTab.get( "init" ));
	  	 	autoSendStateExecMsg();
	  		//QActorContext.terminateQActorSystem(this);//todo
		} 	
		/* 
		* ------------------------------------------------------------
		* PLANS
		* ------------------------------------------------------------
		*/    
	    //genAkkaMshHandleStructure
	    protected void initStateTable(){  	
	    	stateTab.put("handleToutBuiltIn",handleToutBuiltIn);
	    	stateTab.put("init",init);
	    	stateTab.put("firstRequest",firstRequest);
	    	stateTab.put("handleAnswer",handleAnswer);
	    	stateTab.put("secondRequest",secondRequest);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "caller tout : stops");  
	    		repeatPlanNoTransition(pr,myselfName,"application_"+myselfName,false,false);
	    	}catch(Exception e_handleToutBuiltIn){  
	    		println( getName() + " plan=handleToutBuiltIn WARNING:" + e_handleToutBuiltIn.getMessage() );
	    		QActorContext.terminateQActorSystem(this); 
	    	}
	    };//handleToutBuiltIn
	    
	    StateFun init = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("init",-1);
	    	String myselfName = "init";  
	    	temporaryStr = "\"caller STARTS\"";
	    	println( temporaryStr );  
	    	//switchTo firstRequest
	        switchToPlanAsNextState(pr, myselfName, "caller_"+myselfName, 
	              "firstRequest",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun firstRequest = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("firstRequest",-1);
	    	String myselfName = "firstRequest";  
	    	temporaryStr = "\"firstRequest send request 1\"";
	    	println( temporaryStr );  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"arequest(REQUEST,DATA)","arequest(r1,d(1))", guardVars ).toString();
	    	sendMsg("arequest","called1", QActorContext.dispatch, temporaryStr ); 
	    	//bbb
	     msgTransition( pr,myselfName,"caller_"+myselfName,false,
	          new StateFun[]{stateTab.get("handleAnswer") }, 
	          new String[]{"true","M","answer" },
	          30000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_firstRequest){  
	    	 println( getName() + " plan=firstRequest WARNING:" + e_firstRequest.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//firstRequest
	    
	    StateFun handleAnswer = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleAnswer",-1);
	    	String myselfName = "handleAnswer";  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("answer(R,D)");
	    	if( currentMessage != null && currentMessage.msgId().equals("answer") && 
	    		pengine.unify(curT, Term.createTerm("answer(REQUEST,DATA)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "answer1_at_caller(R,D)";
	    		/* Print */
	    		parg =  updateVars( Term.createTerm("answer(REQUEST,DATA)"), 
	    		                    Term.createTerm("answer(R,D)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) println( parg );
	    	}
	    	//switchTo secondRequest
	        switchToPlanAsNextState(pr, myselfName, "caller_"+myselfName, 
	              "secondRequest",false, false, null); 
	    }catch(Exception e_handleAnswer){  
	    	 println( getName() + " plan=handleAnswer WARNING:" + e_handleAnswer.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleAnswer
	    
	    StateFun secondRequest = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("secondRequest",-1);
	    	String myselfName = "secondRequest";  
	    	temporaryStr = "\"firstRequest send request 2\"";
	    	println( temporaryStr );  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"arequest(REQUEST,DATA)","arequest(r2,d(2))", guardVars ).toString();
	    	sendMsg("arequest","called1", QActorContext.dispatch, temporaryStr ); 
	    	//bbb
	     msgTransition( pr,myselfName,"caller_"+myselfName,false,
	          new StateFun[]{() -> {	//AD HOC state to execute an action and resumeLastPlan
	          try{
	            PlanRepeat pr1 = PlanRepeat.setUp("adhocstate",-1);
	            //ActionSwitch for a message or event
	             if( currentMessage.msgContent().startsWith("answer") ){
	            	String parg = "answer2_at_caller(R,D)";
	            	/* Print */
	            	parg =  updateVars( Term.createTerm("answer(REQUEST,DATA)"), 
	            	                    Term.createTerm("answer(R,D)"), 
	            		    		  	Term.createTerm(currentMessage.msgContent()), parg);
	            	if( parg != null ) println( parg );
	             }
	            repeatPlanNoTransition(pr1,"adhocstate","adhocstate",false,true);
	          }catch(Exception e ){  
	             println( getName() + " plan=secondRequest WARNING:" + e.getMessage() );
	             //QActorContext.terminateQActorSystem(this); 
	          }
	          }
	          }, 
	          new String[]{"true","M","answer" },
	          30000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_secondRequest){  
	    	 println( getName() + " plan=secondRequest WARNING:" + e_secondRequest.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//secondRequest
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}