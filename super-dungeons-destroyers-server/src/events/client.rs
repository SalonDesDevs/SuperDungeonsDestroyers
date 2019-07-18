use super::common::Direction;

pub struct Move {
    pub direction: Direction
}

pub enum Event {
    Move(Move)
}
