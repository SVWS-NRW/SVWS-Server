<template>
	<div class="h-full flex flex-col">
		<h1 class="secondary-menu--headline">
			<slot name="headline" />
			<div v-if="$slots.abschnitt" class="input--schule-abschnitte">
				<slot name="abschnitt" />
			</div>
		</h1>
		<div class="secondary-menu--header">
			<slot name="header" />
		</div>
		<div class="secondary-menu--content">
			<slot name="content" />
		</div>
	</div>
</template>

<style lang="postcss">
.secondary-menu--headline {
	@apply py-2 text-headline;
	@apply relative z-20;
}

.app-layout--secondary-container .v-table th:last-child,
.app-layout--secondary-container .v-table td:last-child {
	@apply border-r-0;
}

.secondary-menu--header {
	@apply flex-shrink-0;
	@apply relative z-10;
}

.secondary-menu--content {
	@apply flex-grow;
	@apply overflow-y-auto;
	@apply relative z-0;
	-webkit-overflow-scrolling: touch;
	margin-bottom: -1px;
}

.secondary-menu--content .secondary-menu--navigation {
	@apply flex flex-col items-start
}

.input {
	@apply cursor-pointer;
}

.input--schule-abschnitte {
	@apply -mt-1;
}

.input--schule-abschnitte .dropdown-icon:hover span.icon,
.input--schule-abschnitte .dropdown-icon:focus span.icon {
	@apply bg-transparent text-black;
}

.input--schule-abschnitte input.text-input--control {
	@apply border-0 p-0 h-auto;
	@apply font-bold;
	width: 7.8em;
	padding-right: 1.4rem !important;
}

.input--schule-abschnitte input.text-input--control,
.input--schule-abschnitte .text-input-readonly input.text-input--control {
	@apply cursor-pointer;
}

.input--schule-abschnitte {
	@apply cursor-pointer opacity-25;
}

.input--schule-abschnitte:hover,
.input--schule-abschnitte:focus-within {
	@apply opacity-100;
}

.input--schule-abschnitte .multiselect--items-wrapper {
	@apply mt-2;
	min-width: 12em;
}

.secondary-menu--breadcrumbs {
	@apply flex flex-wrap;

	> *:not(:last-child) {
		@apply opacity-25 cursor-pointer;

		&:hover {
			@apply opacity-100 text-primary;

			&:after {
				@apply opacity-25;
			}
		}

		&:after {
			@apply inline-block mx-1 text-black;
			content: '/';
		}
	}
}
</style>
