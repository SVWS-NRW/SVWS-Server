<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="returnToSchule">Schule</a>
				<span title="Kataloge">Kataloge</span>
			</nav>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #header />
		<template #content>
			<div class="secondary-menu--navigation">
				<template v-for="child_item in children" :key="child_item.name">
					<svws-ui-menu-item @click="setChild(child_item)">
						<template #label> <span>{{ child_item.text }}</span> </template>
					</svws-ui-menu-item>
					<svws-ui-spacing v-if="child_item.name === 'schule.kataloge.foerderschwerpunkte' || child_item.name === 'schule.kataloge.zeitraster'" />
				</template>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { KatalogeAuswahlProps } from "./SKatalogeAuswahlProps";

	const props = defineProps<KatalogeAuswahlProps>();

</script>
