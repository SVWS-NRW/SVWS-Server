<template>
	<template v-if="bezeichnung_abiturjahr">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="props.gruppenprozesseEnabled">
						<h2 class="svws-headline"> Gruppenprozesse für die Abiturjahrgänge </h2>
						<span class="svws-subline"> {{ selected().map(jg => "" + jg.abiturjahr).join(", ") }}</span>
					</template>
					<template v-else-if="props.creationModeEnabled">
						<h2 class="svws-headline"> Anlegen von Abiturjahrgängen... </h2>
						<span class="svws-subline">&nbsp;</span>
					</template>
					<template v-else>
						<template v-if="(auswahl === undefined) || (auswahl.abiturjahr < 0)">
							<h2 class="svws-headline"> {{ bezeichnung_abiturjahr }} </h2>
							<span class="svws-subline">&nbsp;</span>
						</template>
						<template v-else>
							<h2 class="svws-headline"> Abiturjahrgang {{ auswahl.abiturjahr }} </h2>
							<span class="svws-subline"> Jahrgang {{ auswahl.jahrgang }}, {{ schuljahresabschnitt().abschnitt }}.Halbjahr </span>
						</template>
					</template>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-tab-bar :tab-manager :class="`router--tab--${tabManager().tab.name} router--tab--${tabManager().tab.name.replace('.', '_')}`" enable-focus-switching>
			<router-view />
		</svws-ui-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-graduation-cap-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { GostAppProps } from "./SGostAppProps";

	const props = defineProps<GostAppProps>();

	const bezeichnung_abiturjahr = computed(() => props.auswahl?.bezeichnung);

</script>

<style lang="postcss">

	.page--wrapper {
		@apply flex flex-col;
	}

	.router--tab--gost\.kursplanung {
		.svws-ui-tab-content {
			@apply h-full overflow-hidden;
		}
	}

</style>
