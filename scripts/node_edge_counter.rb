require 'bio'
file = File.read("../testData/bsu00010.xml")
kgml = Bio::KEGG::KGML.new(file)

puts "node number:"
# map contains TITLE: node so minus 1
puts kgml.entries.size - 1

map_ids = Array.new
kgml.entries.each do |entry|
  if entry.category == "map"
    map_ids.push(entry.entry_id)
  end
end
p map_ids

puts "edge number:"
edge_num = 0

kgml.reactions.each do |reaction|
  if reaction.direction == "reversible"
    edge_num = edge_num + (2 * reaction.substrates.size) + (2 * reaction.products.size)
  elsif reaction.direction == "irreversible"
    edge_num = edge_num + reaction.substrates.size + reaction.products.size
  end
end
puts edge_num

maplinks = Array.new
kgml.relations.each do |relation|
  if relation.rel == "maplink"
    maplink = Array.new
    if map_ids.include?(relation.node1)
      maplink.push(relation.node1)
      maplink.push(relation.value)
    elsif map_ids.include?(relation.node2)
      maplink.push(relation.node2)
      maplink.push(relation.value)
   end
   maplinks.push(maplink)
  end
end

p maplinks.uniq
edge_num = edge_num + (2 * maplinks.uniq.size)
puts edge_num
