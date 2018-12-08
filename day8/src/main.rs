use std::io;
use std::io::prelude::*;

struct Node {
    children: Vec<Node>,
    metadata: Vec<usize>,
}

fn main() {
    let mut lines = vec![];

    let stdin = io::stdin();
    for line in stdin.lock().lines() {
        let s = line.unwrap();
        lines.push(s);
    }

    let nums: Vec<usize> = lines[0].split(" ").map(|s| s.parse().unwrap()).collect();

    let (root, _) = make_node(&nums[..]);

    // breadth_first_print(&root, 0);

    println!("Part 1: {}", metadata_sum(&root));
    println!("Part 2: {}", node_value(&root));
}

fn metadata_sum(node: &Node) -> usize {
    let sum: usize = node.metadata.iter().sum();
    let children_sum: usize = node.children.iter().map(|child| metadata_sum(child)).sum();

    sum + children_sum
}

fn node_value(node: &Node) -> usize {
    if node.children.len() == 0 {
        return metadata_sum(node);
    }

    let mut value = 0;

    for index in node.metadata.iter() {
        let child = match node.children.get(*index - 1) {
            Some(n) => n,
            None => continue,
        };

        value += node_value(child);
    }

    value
}

fn make_node(nums: &[usize]) -> (Node, usize) {
    let mut begin = 2;
    let mut children = vec![];

    for _i in 0..nums[0] {
        let (node, new_begin) = make_node(&nums[begin..]);
        children.push(node);
        begin += new_begin
    }

    let mut metadata = vec![0; nums[1]];
    metadata.copy_from_slice(&nums[begin..(begin + nums[1])]);

    (Node {children: children, metadata: metadata}, begin + nums[1])
}

fn breadth_first_print(node: &Node, depth: usize) {
    println!("{}metadata: {:?}", "\t".repeat(depth), node.metadata);

    for child in node.children.iter() {
        breadth_first_print(child, depth + 1);
    }
}
