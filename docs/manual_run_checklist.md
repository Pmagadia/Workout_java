# TP03 Manual Run Checklist (Submission Proof)

Use this checklist during final validation before submission.

## Environment
- [ ] From repo root: `e:\CS132\Workout_java`
- [ ] Java compiles cleanly
- [ ] App starts without exceptions

## Compile + Test
- [ ] Run compile:
  - `javac -d target\classes src\main\java\com\mycompany\app\*.java`
- [ ] Run storage/integration tests:
  - `java -cp target\classes com.mycompany.app.FileStorageTest`
- [ ] Confirm terminal ends with: `All FileStorage tests passed.`

## Manual App Flow (Required Features)
- [ ] Startup asks for username
- [ ] Startup asks: Create New (1) or Load Existing (2)
- [ ] Add Workout validates:
  - [ ] Date format `MM/DD/YYYY`
  - [ ] Weight > 0
  - [ ] Reps > 0
  - [ ] Sets > 0
  - [ ] Invalid numeric input re-prompts (no crash)
- [ ] View Workouts lists workouts with indices
- [ ] Edit Workout updates all fields (date, exercise, weight, reps, sets, note, completed)
- [ ] Delete Workout asks for confirmation before removal
- [ ] Search Workouts by Date shows matches or "No workouts found"
- [ ] Exit triggers save message
- [ ] Load Existing shows persisted workouts on next run

## Screenshot Targets (for report)
1. Startup screen showing username + Create/Load option
2. Menu showing options 1-6
3. Add Workout with valid entry
4. Input validation message (bad date or non-numeric input)
5. Edit Workout success
6. Delete confirmation + success
7. Search by date results
8. Exit save message + next run load confirmation
9. `FileStorageTest` passing output

## Artifact
- Terminal proof transcript file: `docs/manual_run_transcript.txt`
