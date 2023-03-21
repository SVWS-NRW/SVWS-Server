<template>
	<svws-ui-secondary-menu>
		<template #headline>Schule</template>
		<template #header />
		<template #content>
			<div class="secondary-menu--navigation">
				<template v-for="child_item in children" :key="child_item.name">
						<svws-ui-menu-item @click="setChild(child_item)">
							<template #label> <span>{{ child_item.text }}</span> </template>
						</svws-ui-menu-item>
				</template>
			</div>
			<div class="secondary-menu--navigation">
				<!--TODO: Menüpunkte dynamisch aus SSchuleDatenaustauschAuswahlProps hier listen-->
				<h3 class="secondary-menu--navigation--headline">Datenaustausch</h3>
				<svws-ui-menu-item disabled>
					<template #label><span>Kurs24</span></template>
				</svws-ui-menu-item>
				<svws-ui-menu-item disabled>
					<template #label><span>Laufbahnplanung</span></template>
				</svws-ui-menu-item>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">
	import { SchuleAuswahlProps } from './SSchuleAuswahlProps';
	const props = defineProps<SchuleAuswahlProps>();
</script>
