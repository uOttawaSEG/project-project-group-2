package ca.seg2105project.model.registrationRequestClasses;

/**
* An enumeration class to hold the three possible states of an AccountRegistrationRequest (PENDING, REJECTED, or APPROVED).
*/
public enum AccountRegistrationRequestStatus {

	/**
	* A pending request.
	*/
	PENDING,

	/**
	* A rejected request.
	*/
	REJECTED,

	/**
	* An approved request.
	*/
	APPROVED
}