/* Generated by AN DISI Unibo */ 
package it.unibo.ctxprj0
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "prj0.pl", "sysRules.pl"
	)
}

