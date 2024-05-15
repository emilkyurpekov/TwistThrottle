package twistthrottle.models.entities.enums;

public enum categoryType {
    EXHAUST_SYSTEMS,
    ENGINE_COMPONENTS,
    BRAKING_COMPONENTS,
    SUSPENSION_AND_STEERING,
    TIRES_AND_WHEELS,
    ELECTRICAL_SYSTEMS,
    BODY_AND_FRAME,
    TRANSMISSION_AND_DRIVETRAIN,
    ACCESSORIES,
    MAINTENANCE_AND_TOOLS;

    @Override
    public String toString() {
        switch (this) {
            case EXHAUST_SYSTEMS:
                return "Exhaust Systems";
            case ENGINE_COMPONENTS:
                return "Engine Components";
            case BRAKING_COMPONENTS:
                return "Braking Components";
            case SUSPENSION_AND_STEERING:
                return "Suspension and Steering";
            case TIRES_AND_WHEELS:
                return "Tires and Wheels";
            case ELECTRICAL_SYSTEMS:
                return "Electrical Systems";
            case BODY_AND_FRAME:
                return "Body and Frame";
            case TRANSMISSION_AND_DRIVETRAIN:
                return "Transmission and Drivetrain";
            case ACCESSORIES:
                return "Accessories";
            case MAINTENANCE_AND_TOOLS:
                return "Maintenance and Tools";
            default:
                throw new IllegalArgumentException("Unknown Category: " + this);
        }
    }
}