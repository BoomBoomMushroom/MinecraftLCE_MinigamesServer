package me.dillionweaver.lobbymanager.data;

public class FoodBalance {
    public FoodBalance(
        int MaxFood,
        double MaxSaturation,
        double StartSaturation,
        double SaturationFloor,
        double ExhaustionDrop,
        int HealthTickCount,
        int HealthTickCountSaturated,
        int HealLevel,
        int StarveLevel,
        int SprintLevel,
        double ExhaustionHeal,
        double ExhaustionJump,
        double ExhaustionSprintJump,
        double ExhaustionMine,
        double ExhaustionAttack,
        double ExhaustionDamage,
        double ExhaustionSneak,
        double ExhaustionWalk,
        double ExhaustionSprint,
        double ExhaustionSwim,
        double ExhaustionIdle
    ){
        this.MaxFood = MaxFood;
        this.MaxSaturation = MaxSaturation;
        this.StartSaturation = StartSaturation;
        this.SaturationFloor = SaturationFloor;
        this.ExhaustionDrop = ExhaustionDrop;
        this.HealthTickCount = HealthTickCount;
        this.HealthTickCountSaturated = HealthTickCountSaturated;
        this.HealLevel = HealLevel;
        this.StarveLevel = StarveLevel;
        this.SprintLevel = SprintLevel;
        this.ExhaustionHeal = ExhaustionHeal;
        this.ExhaustionJump = ExhaustionJump;
        this.ExhaustionSprintJump = ExhaustionSprintJump;
        this.ExhaustionMine = ExhaustionMine;
        this.ExhaustionAttack = ExhaustionAttack;
        this.ExhaustionDamage = ExhaustionDamage;
        this.ExhaustionSneak = ExhaustionSneak;
        this.ExhaustionWalk = ExhaustionWalk;
        this.ExhaustionSprint = ExhaustionSprint;
        this.ExhaustionSwim = ExhaustionSwim;
        this.ExhaustionIdle = ExhaustionIdle;
    }

    public int MaxFood = 20;
    public double MaxSaturation = 20.0;
    public double StartSaturation = MaxSaturation / 4.0;
    public double SaturationFloor = MaxSaturation / 8.0;
    public double ExhaustionDrop = 4.0;
    public int HealthTickCount = 80;
    public int HealthTickCountSaturated = 10;
    public int HealLevel = 18;
    public int StarveLevel = 0;
    public int SprintLevel = 6;

    public double ExhaustionHeal = 6.0;
    public double ExhaustionJump = 0.05;
    public double ExhaustionSprintJump = ExhaustionJump * 4;
    public double ExhaustionMine = 0.005;
    public double ExhaustionAttack = 0.1;
    public double ExhaustionDamage = 0.1;
    public double ExhaustionSneak  = 0.0;
    public double ExhaustionWalk = 0.0;
    public double ExhaustionSprint = 0.1;
    public double ExhaustionSwim = 0.01;
    public double ExhaustionIdle = 0.0;
}
