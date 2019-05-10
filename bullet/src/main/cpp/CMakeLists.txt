cmake_minimum_required(VERSION 3.4.1)

FILE(GLOB_RECURSE bullet-lib-sources
    bullet/LinearMath/*.cpp
    bullet/BulletSoftBody/*.cpp
    bullet/BulletInverseDynamics/*.cpp
    bullet/BulletDynamics/*.cpp
    bullet/BulletCollision/*.cpp
    bullet/Bullet3Serialize/*.cpp
    bullet/Bullet3Geometry/*.cpp
    bullet/Bullet3Dynamics/*.cpp
    bullet/Bullet3Common/*.cpp
    bullet/Bullet3Collision/*.cpp
)
add_library(	bullet-lib
				STATIC
				${bullet-lib-sources}
)

find_library(   log-lib
                log )

FILE(GLOB bullet-wrapper-lib-sources *.cpp)
add_library(	bullet-wrapper-lib
				SHARED
				${bullet-wrapper-lib-sources}
)

target_include_directories (bullet-lib PUBLIC ${CMAKE_CURRENT_LIST_DIR}/bullet)
target_include_directories (bullet-wrapper-lib PUBLIC ${CMAKE_CURRENT_LIST_DIR})

target_link_libraries(bullet-wrapper-lib bullet-lib)
target_link_libraries(bullet-wrapper-lib ${log-lib})