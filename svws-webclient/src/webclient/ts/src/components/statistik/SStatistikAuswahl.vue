<template>
	<svws-ui-secondary-menu>
		<template #headline>Statistik</template>
		<template #header> </template>
		<template #content>
			<div class="secondary-menu--navigation container">
				<svws-ui-sidebar-menu-item
					><template #label
						><span>Verschlüsselung</span></template
					></svws-ui-sidebar-menu-item
				>
				<svws-ui-sidebar-menu-item
					><template #label
						><span>Datenprüfung</span></template
					></svws-ui-sidebar-menu-item
				>
				<svws-ui-sidebar-menu-item
					><template #label
						><span>Dokumente</span></template
					></svws-ui-sidebar-menu-item
				>
				<svws-ui-sidebar-menu-item
					><template #label
						><span>Hilfe</span></template
					></svws-ui-sidebar-menu-item
				>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts"></script>
