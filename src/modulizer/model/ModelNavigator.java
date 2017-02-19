package modulizer.model;

import uflow.data.base.DataList;
import uflow.data.base.DataMap;
import uflow.data.common.immutable.Id;
import uflow.data.function.immutable.ProceedFunction;
import uflow.data.function.immutable.ProcessFunction;
import uflow.data.function.immutable.ProvideFunction;
import uflow.data.function.immutable.RequireFunction;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;
import uflow.data.model.immutable.ProcessUnitModel;

import java.util.ArrayList;

/**
 * Created by augus on 05.01.2017.
 * Bietet Methoden um durch ein UFlow Modell zu Navigieren. Es können einzelne Prozessschritte gesucht werden. Zu den
 * Prozessschritten können dessen Vorgänger und Nachfolger gefunden werden. Und dies in Abhängigkeit, ob es sich um
 * einen DataObjekt verweis, oder einen "Next" verweis handelt.
 * Zusätzlich bildet diese Klasse den SESE (Single Enrty, Single Exit) Algorithmus ab.
 */
public class ModelNavigator {

    private ProcessModel m;
    private boolean dataObjectFlows;
    private int minimalSteps;

    /**
     * @param m Das Modell, durch das Navigiert werden soll
     * @param dataObjectFlows Wenn ture, dann werden DataObjektFlows als Verweis zu nächstem Step gewertet
     * @param minimalSteps Anzahl der Steps, die midestens Notwendig sind, damit ein Modell geteilt wird
     */
    public ModelNavigator(ProcessModel m, boolean dataObjectFlows, int minimalSteps) {
        this.m               = m;
        this.dataObjectFlows = dataObjectFlows;
        this.minimalSteps    = minimalSteps;
    }

    /**
     * @param m
     * @param dataObjectFlows
     */
    public ModelNavigator(ProcessModel m, boolean dataObjectFlows) {
        this.m               = m;
        this.dataObjectFlows = dataObjectFlows;
        this.minimalSteps    = 0;
    }


    /**
     * Liefert das SESE Ende zum übergebenen Step. Falls kein SESE Ende vorhanden ist, wird null zurückgegeben.
     * @param entry
     * @return
     */
    public ProcessStepModel getSESEExitToEntry (ProcessStepModel entry) {
        if (getPrevSteps(entry).size() >= 2)
            return null; // Start ist kein Start. Darf nur einen Vorgänger haben.

        ProcessStepModel ret = null;

        ArrayList<ProcessStepModel> visited = new ArrayList<ProcessStepModel>();

        for (ProcessStepModel next : getNextSteps(entry))
            if (isStepBeforeStep(next, entry))
                return null; // Der Startstep darf kein Vorgänger seiner Nachfolger sein (nicht in einer Schleife)

        for (ProcessStepModel next : getNextSteps(entry)) {
            ret = getSESEExitToEntry(entry, next, visited);
            if (ret != null && getStepsBetweenSteps(entry, ret) >= minimalSteps)
                return ret;
            else if (ret != null && getStepsBetweenSteps(entry, ret) < minimalSteps)
                return null;
        }

        return ret;
    }

    /**
     * Liefert die Anzahl der Prozessschritte, welche zwischen den Beiden übergenenen Prozessschritten sind
     * @param step1
     * @param step2
     * @return
     */
    public int getStepsBetweenSteps(ProcessStepModel step1, ProcessStepModel step2) {
        ArrayList<ProcessStepModel> visited = new ArrayList<ProcessStepModel>();

        isExitToEntryForward(step1, step2, visited);

        return visited.size();
    }

    /**
     * Rekursive Methode zum finden des SESE Exits
     * @param entry
     * @param step
     * @return
     */
    private ProcessStepModel getSESEExitToEntry (ProcessStepModel entry, ProcessStepModel step, ArrayList<ProcessStepModel> visited) {
        ProcessStepModel ret = null;

        visited.add(step);

        // Ist nur ein Exit, wenn nur 1, oder kein Nachfolger ist
        if (getNextSteps(step).size() <= 1)
            if (isExitToEntry(entry, step)) return step;

        for (ProcessStepModel next: getNextSteps(step)) {
            if (isStepInArrayList(next, visited))
                ; //System.out.println("!!Forward: Schleife entdeckt! ("+next.getName()+")");
            else
                ret = getSESEExitToEntry(entry, next, visited);
            if (ret != null) return ret;
        }

        return ret;
    }

    /**
     * Überprüft, ob der Übergebene SESE Anfang zum Übergebnen SESE Ende passt.
     * @param entry
     * @param exit
     * @return
     */
    public boolean isExitToEntry(ProcessStepModel entry, ProcessStepModel exit) {

        boolean ret = true;
        ArrayList<ProcessStepModel> visited = new ArrayList<ProcessStepModel>();

        System.out.println("Start Forward ***************************************************************************");
        ret = ret && isExitToEntryForward(entry, exit, visited);
        if (!ret) return false;

        visited = new ArrayList<ProcessStepModel>();
        System.out.println("Start Backward **************************************************************************");
        ret = ret && isExitToEntryBackward(entry, exit, visited);

        return ret;
    }

    /**
     * Überprüfen, ob alle nachfolgenden Pfade des Eingangsschrittes zum Ausgangsschritt führen. Wenn false, dann handelt
     * es sich nicht um einen SESE Eingang, bzw. SESE Ausgang.
     * @param entry
     * @param exit
     * @return
     */
    private boolean isExitToEntryForward(ProcessStepModel entry, ProcessStepModel exit, ArrayList<ProcessStepModel> visited) {
        visited.add(entry);

        if (entry == null)                      return false;
        if (entry.equals(exit))                 return true;
        if (getNextSteps(entry).size() == 0)    return false;

        boolean ret = true;
        for (ProcessStepModel next: getNextSteps(entry)) {
            if (isStepInArrayList(next, visited))
                ;
            else
                ret = ret && isExitToEntryForward(next, exit, visited);
        }

        return ret;
    }

    /**
     * Überprüfen, ob alle Pfade rückwerts (der Vorgänger) zu entry führen
     * @param entry
     * @param exit
     * @return
     */
    private boolean isExitToEntryBackward(ProcessStepModel entry, ProcessStepModel exit, ArrayList<ProcessStepModel> visited) {
        visited.add(exit);

        if (exit == null)                      return false;
        if (entry.equals(exit))                return true;
        if (getPrevSteps(exit).size() == 0)    return false;

        boolean ret = true;
        for (ProcessStepModel prev: getPrevSteps(exit)) {
            if (isStepInArrayList(prev, visited))
                ;

            else
                ret = ret && isExitToEntryBackward(entry, prev, visited);
        }

        return ret;
    }

    /**
     * Hiltsmethode. Überprüft ob ein ProcessStepModel Objekt in einer Arraylist aus ProcessStepModel vorhanden ist
     * @param step
     * @param visited
     * @return
     */
    private boolean isStepInArrayList(ProcessStepModel step, ArrayList<ProcessStepModel> visited) {
        for (ProcessStepModel x : visited)
            if (x.equals(step))
                return true;

        return false;
    }

    /**
     * Überprüft, ob step1 im Prozessmodell vohr step2 ausgeführt wird. Dient zum erkennen von Schleifen im
     * Prozessmodell. Zum finden wird die Rekursive Methode isStepBeforeStep_2 aufgerufen
     * @param step1
     * @param step2
     * @return
     */
    public boolean isStepBeforeStep(ProcessStepModel step1, ProcessStepModel step2) {
        ArrayList<ProcessStepModel> visited = new ArrayList<>();

        return isStepBeforeStep_2(step1, step2, visited);
    }

    /**
     * Liefert true zruück, wenn step1 im Modell vor step2 ist, auch wenn es kein direkter Vorgänger ist
     * @param step1
     * @param step2
     * @return
     */
    private boolean isStepBeforeStep_2(ProcessStepModel step1, ProcessStepModel step2, ArrayList<ProcessStepModel> visited) {
        if (step2 == null)          return false;
        if (step1.equals(step2))    return true;

        for (ProcessStepModel visitedstep : visited)
            if (visitedstep.equals(step2))
                return false;

        visited.add(step2);

        boolean ret = false;
        for (ProcessStepModel prev: getPrevSteps(step2)) {
            if (prev.equals(step1)) { return true; } // Ist Vorgänger
            ret = ret || isStepBeforeStep_2(step1, prev, visited);
        }

        return ret;
    }

    /**
     * Liefert true zruück, wenn step1 im Modell nach step2 ist, auch wenn es kein direkter Nachfolger ist
     * @param step1
     * @param step2
     * @return
     */
    @Deprecated
    public boolean isStepAfterStep(ProcessStepModel step1, ProcessStepModel step2) {
        if (step2 == null)                      return false;
        if (step1.equals(step2))                return true;
        if (getPrevSteps(step2).size() == 0)    return false;

        boolean ret = false;
        for (ProcessStepModel prev: getNextSteps(step2))
            ret = ret || isStepAfterStep(step1, prev);

        return ret;
    }

    /**
     * Gibt textuell das gesamte Modell aus
     * @return
     */
    @Deprecated
    public String printModel() {

        for (ProcessStepModel step: getFirstSteps())
            printStep(step);

        return null;
    }

    /**
     * Rekursive Methode zur ausgabe des Modelles
     * @param step
     */
    @Deprecated
    private void printStep(ProcessStepModel step) {
        System.out.println(step);
        for (ProcessStepModel next: getNextSteps(step))
            if (next != null)
                printStep(next);
    }

    /**
     * Liefert alle Start Steps (aller Units) in einer ArrayList zurück
     * @return
     */
    public ArrayList<ProcessStepModel> getFirstSteps() {
        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            if (unit.getStartProcessStep() != null)
                steps.add(getStep(new Id("ProcessStepModel", unit.getStartProcessStep(), unit.getId().getContext() + "/" + unit.getId().getKey())));

        return steps;
    }

    /**
     * Liefert das Step Objekt des übergebenen Strings (=Name) zurück
     * @param step_id
     * @return
     * @deprecated
     */
    public ProcessStepModel getStep(String step_id) {
        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            for (ProcessStepModel step : unit.getProcessStepModels().getValues())
                if (step.getName().equals(step_id))
                    return step;
        return null;
    }

    /**
     * Liefert das Step Objekt mit der übergenen id (=Id Objekt) zurück
     * @param id
     * @return
     */
    public ProcessStepModel getStep (Id id) {
        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            for (ProcessStepModel step : unit.getProcessStepModels().getValues())
                if (step.getId().equals(id))
                    return step;
        return null;
    }

    /**
     * Liefert alle direkten vorgänger Steps des übergenen Steps. Abhängig vom im Konstruktor gesetzten Parameter
     * dataObjectFlow, werden auch DataObjectFlows als Vorgänger gewertet.
     * @param step
     * @return
     */
    public ArrayList<ProcessStepModel> getPrevSteps(ProcessStepModel step) {
        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        steps = getPrevStepsNext(step);

        if (this.dataObjectFlows) {
            steps.addAll(getPrevStepsDO(step));
        }

        return steps;
    }

    /**
     * Liefert alle Vorgänger des übergebenen Step Objektes zurück, welche mit "getNext(..)" auf dieses Step verweisen
     * @param step
     * @return
     */
    public ArrayList<ProcessStepModel> getPrevStepsNext(ProcessStepModel step) {
        ArrayList<ProcessStepModel> steps = new ArrayList<>();
        Id id = null;

        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            for (ProcessStepModel step_tmp : unit.getProcessStepModels().getValues())
                for (ProcessFunction func : step_tmp.getProcessFunctions()) {
                    if (func.getClass() == ProceedFunction.class) {
                        if (((ProceedFunction) func).getTargetProcessUnit().equals(""))
                            id = new Id("ProcessStepModel", ((ProceedFunction) func).getNext(), step.getId().getContext());
                        else
                            id = new Id("ProcessStepModel", ((ProceedFunction) func).getNext(), unit.getId().getContext() + "/" + ((ProceedFunction) func).getTargetProcessUnit());

                        if (getStep(id) != null && getStep(id).equals(step))
                            steps.add(step_tmp);
                    }
                }
        return steps;
    }

    /**
     * Liefert alle DataObjectFlow Vorgänger des übergenen steps zurück.
     * @param step
     * @return
     */
    public ArrayList<ProcessStepModel> getPrevStepsDO(ProcessStepModel step) {
        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        /* RequireFunction des übergebenen step finden und Unit und Key herausfinden */
        String context = step.getId().getContext();
        String unit = context.substring(context.indexOf('/')+1, context.length());
        String key = "";
        for (ProcessFunction func : step.getProcessFunctions())
            if (func.getClass() == RequireFunction.class)
                for (DataMap.Entry o : (DataMap)func.getDataItem())
                    for (Object i : (DataList) o.getDataList()) {
                        key = i.toString();
                        /* Alle Steps finden, die in ihrer ProvideFunction die Unit und den Key hinterlegt haben */
                        for (ProcessUnitModel u : m.getProcessUnitModels().getValues())
                            for (ProcessStepModel step_tmp : u.getProcessStepModels().getValues())
                                for (ProcessFunction f : step_tmp.getProcessFunctions())
                                    if (f.getClass() == ProvideFunction.class) {
                                        DataMap dm = (DataMap) f.getDataItem();
                                        if (dm.get("To").toString().equals(unit) && dm.get("Key").toString().equals(key))
                                            steps.add(step_tmp);
                                    }
                    }

        return steps;
    }

    /**
     * Liefert alle direkten nachfolger Steps des übergebnen Steps. Abhänig von der im Konstruktor gesetzten
     * Variable dataObjectFlows, werden auch Empfänger von dataObjectsFlows als nächsten Schritt gewertet.
     * @param step
     * @return
     */
    public ArrayList<ProcessStepModel> getNextSteps(ProcessStepModel step) {
        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        steps = getNextStepsNext(step);

        if (this.dataObjectFlows) {
            steps.addAll(getNextStepsDO(step));
        }

        return steps;
    }

    /**
     * Liefert alle Nachfolger des übergebenen Step Objektes zurück, auf welche mit "getNext(..)" von diesem
     * Step verwiesen wird.
     * @param step
     * @return
     */
    public ArrayList<ProcessStepModel> getNextStepsNext(ProcessStepModel step) {
        if (step == null) return null;

        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        Id id = null;

        for (ProcessFunction func : step.getProcessFunctions()) {
            if (func.getClass() == ProceedFunction.class) {
                String qualifier = step.getId().getQualifier();
                String context = step.getId().getContext();
                String modelId = context.substring(0,context.indexOf('/')+1);
                if (((ProceedFunction)func).getTargetProcessUnit().equals(""))
                    id = new Id(qualifier, ((ProceedFunction)func).getNext(), context);
                else
                    id = new Id(qualifier, ((ProceedFunction)func).getNext(), modelId + ((ProceedFunction)func).getTargetProcessUnit());

                if (getStep(id) != null)
                    steps.add(getStep(id));
            }
        }

        return steps;
    }

    /**
     * Liefert alle DataObjectFlow Nachfolger des übergenen steps zurück.
     * @param step
     * @return
     */
    public ArrayList<ProcessStepModel> getNextStepsDO (ProcessStepModel step) {
        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        for (ProcessFunction func : step.getProcessFunctions()) {
            if (func.getClass() == ProvideFunction.class) {
                DataMap dm = (DataMap) func.getDataItem();
                steps.add(getStepDO(dm.get("To").toString(), dm.get("Key").toString()));
            }
        }

        return steps;
    }

    /**
     * Findet den Step einer Unit, welcher eine RequireFunction besitzt, welche den übergebenen key beinhaltet.
     * @param unit
     * @param key
     * @return
     */
    public ProcessStepModel getStepDO(String unit, String key) {

        /* Finde RequireFunction für den gesuchten Value in der gesuchten Unit */
        ProcessStepModel step = null;

        for (ProcessUnitModel u : m.getProcessUnitModels().getValues() )
            if (u.getName().equals(unit))
                for (ProcessStepModel step_tmp : u.getProcessStepModels().getValues())
                    for (ProcessFunction func : step_tmp.getProcessFunctions()) {
                        if (func.getClass() == RequireFunction.class) {
                            DataMap dm = (DataMap)func.getDataItem();
                            for (DataMap.Entry o : dm)
                                if (o.getDataList().contains(key))
                                    return step_tmp;
                    }
                }

        return step;
    }
}
