<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span class="line-clamp-2">Schule</span>
		</template>
		<template #header />
		<template #content>
			<div class="secondary-menu--navigation">
				<template v-for="child_item in children" :key="child_item.name">
					<svws-ui-menu-item @click="setChild(child_item)">
						<template #label> <span>{{ child_item.text }}</span> </template>
					</svws-ui-menu-item>
					<svws-ui-spacing v-if="child_item.name === 'benutzergruppen'" />
				</template>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { SchuleAuswahlProps } from './SSchuleAuswahlProps';

	const props = defineProps<SchuleAuswahlProps>();

</script>
