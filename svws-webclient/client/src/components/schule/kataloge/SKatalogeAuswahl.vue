<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>
				<nav class="secondary-menu--breadcrumbs">
					<a @click="returnToSchule">Schule</a>
					<span title="Kataloge">Kataloge</span>
				</nav>
			</h1>
			<div class="input--schule-abschnitte">
				<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
			</div>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<div class="secondary-menu--navigation">
				<template v-for="child_item in children" :key="child_item.name">
					<svws-ui-menu-item @click="setChild(child_item)">
						<template #label> <span>{{ child_item.text }}</span> </template>
					</svws-ui-menu-item>
					<svws-ui-spacing v-if="child_item.name === 'schule.kataloge.foerderschwerpunkte' || child_item.name === 'schule.kataloge.zeitraster'" />
				</template>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { KatalogeAuswahlProps } from "./SKatalogeAuswahlProps";

	const props = defineProps<KatalogeAuswahlProps>();

</script>
