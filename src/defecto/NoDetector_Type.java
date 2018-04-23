package defecto;

/* First created by JCasGen Tue Apr 03 22:41:57 CEST 2018 */

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Mon Apr 23 18:01:37 CEST 2018
 * @generated */
public class NoDetector_Type extends Annotation_Type {
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = NoDetector.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("defecto.NoDetector");



  /** @generated */
  final Feature casFeat_idOracion;
  /** @generated */
  final int     casFeatCode_idOracion;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getIdOracion(int addr) {
        if (featOkTst && casFeat_idOracion == null)
      jcas.throwFeatMissing("idOracion", "defecto.NoDetector");
    return ll_cas.ll_getIntValue(addr, casFeatCode_idOracion);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setIdOracion(int addr, int v) {
        if (featOkTst && casFeat_idOracion == null)
      jcas.throwFeatMissing("idOracion", "defecto.NoDetector");
    ll_cas.ll_setIntValue(addr, casFeatCode_idOracion, v);}
    
  
 
  /** @generated */
  final Feature casFeat_oracionString;
  /** @generated */
  final int     casFeatCode_oracionString;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getOracionString(int addr) {
        if (featOkTst && casFeat_oracionString == null)
      jcas.throwFeatMissing("oracionString", "defecto.NoDetector");
    return ll_cas.ll_getStringValue(addr, casFeatCode_oracionString);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setOracionString(int addr, String v) {
        if (featOkTst && casFeat_oracionString == null)
      jcas.throwFeatMissing("oracionString", "defecto.NoDetector");
    ll_cas.ll_setStringValue(addr, casFeatCode_oracionString, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public NoDetector_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_idOracion = jcas.getRequiredFeatureDE(casType, "idOracion", "uima.cas.Integer", featOkTst);
    casFeatCode_idOracion  = (null == casFeat_idOracion) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_idOracion).getCode();

 
    casFeat_oracionString = jcas.getRequiredFeatureDE(casType, "oracionString", "uima.cas.String", featOkTst);
    casFeatCode_oracionString  = (null == casFeat_oracionString) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_oracionString).getCode();

  }
}



    