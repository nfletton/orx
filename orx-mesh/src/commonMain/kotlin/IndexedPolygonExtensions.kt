package org.openrndr.extra.mesh

import org.openrndr.math.LinearType
import org.openrndr.math.Vector3

internal fun <T : LinearType<T>> bc(barycentric: Vector3, items: List<T>): T {
    return (items[0] * barycentric.x) + (items[1] * barycentric.y) + (items[2] * barycentric.z)
}

/**
 * Evaluate a point in triangle
 * @param vertexData the vertex data to use
 * @param barycentric the barycentric coordinates of the point to evaluate
 */
fun IIndexedPolygon.point(vertexData: VertexData, barycentric: Vector3): Point {
    require(positions.size == 3)

    val positions = vertexData.positions.slice(positions)
    val colors = vertexData.colors.slice(colors)
    val normals = vertexData.normals.slice(normals)
    val tangents = vertexData.tangents.slice(tangents)
    val bitangents = vertexData.bitangents.slice(bitangents)
    val textureCoords = vertexData.textureCoords.slice(textureCoords)

    return Point(
        (if (positions.isNotEmpty()) bc(barycentric, positions) else null)!!,
        if (textureCoords.isNotEmpty()) bc(barycentric, textureCoords) else null,
        if (colors.isNotEmpty()) bc(barycentric, colors) else null,
        if (normals.isNotEmpty()) bc(barycentric, normals) else null,
        if (tangents.isNotEmpty()) bc(barycentric, tangents) else null,
        if (bitangents.isNotEmpty()) bc(barycentric, bitangents) else null
    )
}