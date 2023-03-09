<template>
	<svws-ui-secondary-menu>
		<template #headline>Schule</template>
		<template #header />
		<template #content>
			<template v-for="child_item in children" :key="child_item.name">
				<div class="secondary-menu--navigation container">
					<svws-ui-menu-item @click="setChild(child_item)">
						<template #label> <span>{{ child_item.text }}</span> </template>
					</svws-ui-menu-item>
				</div>
			</template>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">
	import { SchuleAuswahlProps } from './SSchuleAuswahlProps';
	const props = defineProps<SchuleAuswahlProps>();
</script>
