package nomic.domain

/**
 * This abstract employs the specification pattern. It seeks to keep the repository focused
 * on data retrieval by abstracting out the components of the query. Thus, the application/domain
 * layer becomes the authority on what specifications make up the necessary query necessary for
 * the business logic it is performing.
 *
 * It also renders possible the reuse and chaining specifications, thereby promoting the
 * DRY principle.
 *
 * Finally, by using the specification pattern, testing becomes significantly easier, as
 * the repository tests can focus on data retrieval and mapping, the specification tests
 * can focus on that particular part of the query, and the application layer tests
 * can focus on correctly orchestrating the calls to ensure that the correct business
 * operation is applied to the situation at hand.
 *
 * Relevant links discussing this design pattern:
 *
 * [DevIQ](https://deviq.com/design-patterns/specification-pattern)
 *
 * [Java Design Pattern: Specification](https://java-design-patterns.com/patterns/specification/)
 *
 * [EnterpiseCraftsmanship : Specification Pattern](https://enterprisecraftsmanship.com/posts/specification-pattern-c-implementation/)
 */
interface ISpecification
