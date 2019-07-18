use super::common::Direction;

#[derive(Debug)]
pub struct Move {
    pub direction: Direction
}

#[derive(Debug)]
pub enum Event {
    Move(Move)
}
