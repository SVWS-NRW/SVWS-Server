package de.nrw.schule.svws.davapi.util.vcard;

/**
 * VCard Property für E-Maildaten.
 * 
 * Beispiel: <br>
 * {@code         EMAIL;TYPE=internet:jqpublic@xyz.dom1.com

        EMAIL;TYPE=internet:jdoe@isp.net

        EMAIL;TYPE=internet,pref:jane_doe@abc.com}
 * 
 * @see <a href=
 *      "https://datatracker.ietf.org/doc/html/rfc2426#section-3.3.2">RFC
 *      Dokumentation zum EMAIL Type</a>
 *
 */
public class EMailProperty implements VCardProperty {

	/**
	 * Constant für den Typ dieses VCardProperties
	 */
	private static final String EMAIL_TYPE = "EMAIL;TYPE=";
	/**
	 * die Mail-Adresse dieses Properties
	 */
	private String mailAddress;
	/**
	 * die Art der Mailadresse, bspw. 'internet,pref'
	 */
	private String mailType;

	/**
	 * Default constructor für das Email Property mit dem mailType und der
	 * Mail-Adresse
	 * 
	 * @param mailType    die Art der Mailadresse
	 * @param mailAddress die Mailadresse
	 */
	public EMailProperty(String mailType, String mailAddress) {
		this.mailType = mailType;
		this.mailAddress = mailAddress;
	}

	/**
	 * getter für die Mail Adresse
	 * 
	 * @return die Mailadresse
	 */
	public String getMailAddress() {
		return mailAddress;
	}

	/**
	 * setter für die Mail Adresse
	 * 
	 * @param mailAddress die Mailadresse
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	/**
	 * getter für den Mail Type, bspw. 'work'
	 * 
	 * @return der Mail Type
	 */
	public String getMailType() {
		return mailType;
	}

	/**
	 * setter für den Mail Type, bspw 'work'
	 * 
	 * @param mailType der Mail Tye
	 */
	public void setMailType(String mailType) {
		this.mailType = mailType;
	}

	@Override
	public String getType() {
		return EMAIL_TYPE + mailType;
	}

	@Override
	public void serializeType(StringBuilder sb) {
		sb.append(getType());
	}

	@Override
	public void serializeProperty(StringBuilder sb) {
		sb.append(mailAddress);
	}
}